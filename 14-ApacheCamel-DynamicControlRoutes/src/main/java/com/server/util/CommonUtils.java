package com.server.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author CYX
 * @date 2018/11/7 14:15
 */
public class CommonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);


    /**
     * JSON字符串转换为Java Bean对象
     *
     * @param json  JSON字符串
     * @param clazz
     * @param <T>
     * @return
     */
    public static final <T> T toJavaObject(Map<String, Object> json, Class<T> clazz) {
        try {
            return new JSONObject(json).toJavaObject(clazz);
        } catch (Exception e) {
            LOGGER.error("json : {} , e : {} , message : {}", json, e, e.getMessage());
            return null;
        }
    }
    /**
     * Java Bean对象转换为JSON字符串
     *
     * @param object Java Bean对象
     * @return
     */
    public static final String toJSONStr(Object object) {
        String jsonStr = null;
        try {
            jsonStr = JSONObject.toJSONString(object);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return jsonStr;
    }

}
