package util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xingmao on 2017/6/26.
 */
@Slf4j
public class HttpClientPoolUtil {
    private static int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 3000;
    private static int DEFAULT_CONNECT_TIMEOUT = 3000;
    private static int DEFAULT_SOCKET_TIMEOUT = 3000;
    private static int DEFAULT_MAX_TOTAL_CONNECTION = 512;
    private static int DEFAULT_MAX_PER_ROUTE_CONNECTION = 32;
    private static int DEFAULT_MAX_TRY_TIMES = 3;
    private static int DEFAULT_MAX_TIMEOUT = 3000;
    private static int DEFAULT_PERFORMANCE_THRESHOLD = 500;
    private static int HTTTP_CODE_OK = 200;

    private static CloseableHttpClient httpClient;

    private final static RequestConfig requestConfig =  RequestConfig.custom()
            .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
            .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
            .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
            .build();

    private final static HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetry();

    private final static ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
    private final static LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
    private final static Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", plainsf)
            .register("https", sslsf)
            .build();

    private final static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);

    static {
        cm.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTION);
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE_CONNECTION);
        httpClient = HttpClients.custom().setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler).build();
    }

    private static void setHeaders(HttpRequestBase httpRequestBase) {
        httpRequestBase.setHeader("User-Agent", "db.auto.sina.cn");
        httpRequestBase.setHeader("Accept","application/json");
        httpRequestBase.setHeader("Accept-Encoding", "gzip, deflate");
        httpRequestBase.setHeader("Accept-Language", "en,zh-CN;q=0.8,zh;q=0.6");
        httpRequestBase.setHeader("Accept-Charset", "utf-8;q=0.7,*;q=0.7");
        // 配置请求的超时设置
        httpRequestBase.setConfig(requestConfig);
    }

    public static String doPost(String url, Map<String,String> parameterMap){
        HttpPost httpPost = new HttpPost(url);
        setHeaders(httpPost);
        int size = parameterMap.size();
        List<NameValuePair> nameValuePairs = new ArrayList<>(size);
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity urlEncodedFormEntity;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("nameValuePairs:{},"+nameValuePairs+ " UrlEncodedFormEntity exception is:{}"+LogExceptionStackUtil.logExceptionStack(e));
            return null;
        }
        httpPost.setEntity(urlEncodedFormEntity);
        return fetchHttpResponse(httpPost);
    }

    public static String doPost(String url, Object object){
        HttpPost httpPost = new HttpPost(url);
        StringEntity s = new StringEntity(JSON.toJSONString(object), Consts.UTF_8);
        s.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setEntity(s);
        httpPost.setConfig(requestConfig);
        return fetchHttpResponse(httpPost);
    }

    public static String doGet(String url){
        HttpGet httpGet = new HttpGet(url);
        setHeaders(httpGet);
        return fetchHttpResponse(httpGet);
    }

    private static String fetchHttpResponse(HttpUriRequest httpUriRequest){
        HttpEntity entity = null;
        String content = null;
        try(CloseableHttpResponse response = httpClient.execute(httpUriRequest, HttpClientContext.create())) {
            if ( response.getStatusLine().getStatusCode() != HTTTP_CODE_OK) {
                log.warn("request Scheme is:{}, host is:{},  url is:{}, StatusCode is:{}, return null", httpUriRequest.getURI().getScheme(),
                        httpUriRequest.getURI().getHost(), httpUriRequest.getURI().getPath(), response.getStatusLine().getStatusCode());
                return null;
            }
            entity = response.getEntity();
            if (null == entity ){
                return null;
            }
            content = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } catch (ClientProtocolException e) {
            log.error("request http interface:{} , failed. Query is:{}, exception is:{}", httpUriRequest.getRequestLine().getUri(),
                    httpUriRequest.getURI().getQuery(), LogExceptionStackUtil.logExceptionStack(e));
        } catch (IOException e) {
            log.error("request http interface:{} , failed. Query is:{}, exception is:{}", httpUriRequest.getRequestLine().getUri(),
                    httpUriRequest.getURI().getQuery(), LogExceptionStackUtil.logExceptionStack(e));
        }
        return content;
    }


    /** 该方法为解决http接口总是调用失败的问题，但仍然在测试中……
     * @param url
     * @return
     */
    public static String doGetTest(String url){
        HttpGet httpGet = new HttpGet(url);
        setHeaders(httpGet);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (ClientProtocolException e) {
            log.error("request http interface url is:{}, failed. response.getEntity() IOException is:{} ", httpGet.getRequestLine().getUri()
                    , LogExceptionStackUtil.logExceptionStack(e));
        } catch (IOException e) {
            log.error("request http interface url is:{}, failed. response.getEntity() IOException is:{} ", httpGet.getRequestLine().getUri()
                    , LogExceptionStackUtil.logExceptionStack(e));
        } finally {
            httpGet.releaseConnection();
        }
        if ( null == response ) {
            log.warn("request url is:{}, response is null, return null", url);
            return null;
        } else {
            int httpCode = response.getStatusLine().getStatusCode();
            if ( httpCode>=400 ) {
                log.warn("http url is:{}, httpCode is:{}", url, httpCode);
                return null;
            }
            try {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            } catch (IOException e) {
                log.error("request http interface url is:{}, failed, return null. response.getEntity() IOException is:{} ", httpGet.getRequestLine().getUri()
                        , LogExceptionStackUtil.logExceptionStack(e));
                return null;
            }
        }
    }


    private static class HttpRequestRetry implements HttpRequestRetryHandler {

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            // 如果已经重试了 DEFAULT_MAX_TRY_TIMES 次，就放弃
            if (executionCount >= DEFAULT_MAX_TRY_TIMES) {
                return false;
            }
            //// 如果服务器丢掉了连接，那么就重试
            if (exception instanceof NoHttpResponseException) {
                return true;
            }
            // 不要重试SSL握手异常
            if (exception instanceof SSLHandshakeException) {
                return false;
            }
            // 超时
            if (exception instanceof InterruptedIOException) {
                return false;
            }
            // 目标服务器不可达
            if (exception instanceof UnknownHostException) {
                return false;
            }
            // 连接被拒绝
            if (exception instanceof ConnectTimeoutException) {
                return false;
            }
            // ssl握手异常
            if ( exception instanceof SSLException ) {
                return false;
            }

            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                return true;
            }
            return false;
        }
    }

}
