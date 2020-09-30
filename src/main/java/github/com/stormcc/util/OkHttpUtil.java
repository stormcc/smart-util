package github.com.stormcc.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.EventListener;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpUtil {
	private static String USER_AGENT= "db.auto.sina.cn";
    private static long PERFORMANCE_THRESHOLD_VALUE_MILLISECONDS = 300L;
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(3000, TimeUnit.MILLISECONDS)       //设置连接超时
            .readTimeout(3000, TimeUnit.MILLISECONDS)          //设置读超时
            .writeTimeout(3000, TimeUnit.MILLISECONDS)          //设置写超时
            .retryOnConnectionFailure(true)             //是否自动重连
            .addInterceptor(new LoggingInterceptor())
            .eventListenerFactory(HttpEventListener.FACTORY)
            .connectionPool(new ConnectionPool(64, 30L, TimeUnit.SECONDS))
            .build();



    public static String getResponse(String url) {
        Request request = buildRequest(url);
        return response(request);
    }



	public static String postResponse(String url,  Map<String, String> paraMap) {
		Request request = buildRequestPost(url, paraMap);
		return response(request);
	}

	public static String postResponse(String url, String body) {
		String defaultType = "text/html";
		Request request = buildRequestPost( url, defaultType,  body);
		return response(request);
    }

	public static String postResponse(String url, String body, String type) {
		Request request = buildRequestPost( url, type,  body);
		return response(request);
	}

	private static Request buildRequestPost(String url, String body, String type){
		RequestBody requestBody = RequestBody.create(MediaType.parse(type), body);
		return  new Request.Builder()
				.url(url)
				.post(requestBody)
				.build();
	}

	private static Request buildRequestPost(String url, Map<String, String> paraMap){
		FormBody.Builder params= new FormBody.Builder();
		Iterator<Map.Entry<String, String>> entries = paraMap.entrySet().iterator();
		Map.Entry<String, String> entry;
		while (entries.hasNext()) {
			entry = entries.next();
			params.add(entry.getKey(), entry.getValue());
		}

		return new Request.Builder()
				.url(url)
				.header("User-Agent", USER_AGENT)
				.post(params.build())
				.build();
	}

	private static String response(Request request) {
		String content;
		try (Response response = okHttpClient.newCall(request).execute()) {
			if (response.isSuccessful()) {
				try {
					ResponseBody body = response.body();
					if ( null == body) {
						return null;
					}
					content = body.string();
				} catch (IOException e) {
					log.warn("url is:{}, IOException is:{}", request.url().url().toString(), LogExceptionStackUtil.logExceptionStack(e));
					return null;
				}
			} else {
				int httpCode = response.code();
				log.warn("request fail, httpCode is:{}, url is:{}, return content null", httpCode, request.url().url().toString());
				return null;
			}
			return content;
		} catch (IOException e) {
			log.warn("request fail, url is:{}, return content is null", request.url().url().toString());
			return null;
		}
	}

    private static Request buildRequest(String url){
        return new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .build();
    }


    static class LoggingInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response =  chain.proceed(request);
            long costTime = System.currentTimeMillis() - startTime;

            long beforeRequestCost = response.sentRequestAtMillis() - startTime;
            if ( beforeRequestCost>2 || costTime> PERFORMANCE_THRESHOLD_VALUE_MILLISECONDS ) {
                log.warn("url is:{}, costTime is:{} ms, beforeRequestCost is:{} ms",
                        response.request().url(), costTime, beforeRequestCost);
            }
            return response;
        }
    }

    static class HttpEventListener  extends EventListener {
        private static long DNS_COST_MAX_MS = 20L;
        /**
         * 自定义EventListener工厂
         */
        static final Factory FACTORY = new Factory() {
            @Override
            public EventListener create(Call call) {
                return new HttpEventListener( System.currentTimeMillis());
            }
        };

        /**
         * 每次请求的标识
         */
        private long dnsStartMilli;
        /**
         * 每次请求的开始时间，单位纳秒
         */
        private long callStartMilli;
        private long dnsCostMilli;

        HttpEventListener(long callStartMilli) {
            this.callStartMilli = callStartMilli;
        }

        @Override
        public void dnsStart(Call call, String domainName) {
            super.dnsStart(call, domainName);
            this.dnsStartMilli = System.currentTimeMillis();
        }

        @Override
        public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
            super.dnsEnd(call, domainName, inetAddressList);
            this.dnsCostMilli = System.currentTimeMillis() - dnsStartMilli;
            if ( log.isDebugEnabled() ) {
                log.debug("domainName:{}, resolve cost:{} ms, dnsStartMilli is:{}, callStartMilli is:{}",
                        domainName, dnsCostMilli, dnsStartMilli, callStartMilli);
            } else {
                if (dnsCostMilli > DNS_COST_MAX_MS) {
                    log.info("domainName:{}, resolve cost:{} ms, dnsStartMilli is:{}, callStartMilli is:{}",
                            domainName, dnsCostMilli, dnsStartMilli, callStartMilli);
                }
            }
        }
    }
}
