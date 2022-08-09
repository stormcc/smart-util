package github.com.stormcc.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.TimeZone;

public final class JacksonUtil {
    private static ObjectMapper serializeObjectMapper = null;
    private static ObjectMapper deserializeObjectMapper = null;

    private JacksonUtil(){}

    public static ObjectMapper serializeObjectMapper(){
        if ( serializeObjectMapper == null ) {
            synchronized (JacksonUtil.class) {
                if ( serializeObjectMapper == null ) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                    // 允许单引号字段名
                    mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                    // 自动给字段名加上引号
//                    mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
                    // 时间默认以时间戳格式写
                    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    // 设置时间转换所使用的默认时区
                    mapper.setTimeZone(TimeZone.getDefault());
                    // null不生成到json字符串中
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true);

                    // 全局日期反序列化配置
                    SimpleModule module = new SimpleModule();
                    module.addDeserializer(java.util.Date.class, new DateDeserializers.DateDeserializer());
                    module.addDeserializer(java.sql.Date.class, new DateDeserializers.SqlDateDeserializer());
                    module.addDeserializer(java.sql.Timestamp.class, new DateDeserializers.TimestampDeserializer());
                    mapper.registerModule(module);
                    serializeObjectMapper = mapper;
                }
            }
        }

        return serializeObjectMapper;
    }

    public static ObjectMapper deserializeObjectMapper(){
        if ( deserializeObjectMapper == null ) {
            synchronized (JacksonUtil.class) {
                if ( deserializeObjectMapper == null ){
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
                    deserializeObjectMapper = mapper;
                }
            }
        }
        return deserializeObjectMapper;
    }
}
