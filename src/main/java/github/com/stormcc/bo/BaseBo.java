package github.com.stormcc.bo;

import github.com.stormcc.util.LogExceptionStackUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class BaseBo implements Serializable {
    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("return null, JsonProcessingException is:{}", LogExceptionStackUtil.logExceptionStack(e));
            return null;
        }
    }
}
