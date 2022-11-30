package com.teraenergy.illegalparking.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.util.enums.JsonUtilModule;
import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Data
public class JsonUtil {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Object 를 JsonNode 로 변환
     *
     * @param object
     * @return
     * @throws TeraException
     */
    public static JsonNode toJsonNode(Object object) throws TeraException {
        return toJsonNode(object, JsonUtilModule.NONE);
    }

    /**
     * Object 를 JsonNode 로 변환
     *
     * @param object
     * @param JsonUtilModule - JsonUtilModule
     * @return
     * @throws TeraException
     */
    public static JsonNode toJsonNode(Object object, JsonUtilModule JsonUtilModule) throws TeraException {
        try {
            if (object instanceof String) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setDateFormat(dateFormat);
                objectMapper.registerModule(new JavaTimeModule());  // LocalDateTime 변환을위한 모듈 등록 (필수)

                switch (JsonUtilModule) {
                    case HIBERNATE:
                        objectMapper.registerModule(new Hibernate5Module());
                        break;
                    default:
                        break;
                }

                return objectMapper.readTree((String) object);
            } else
                return toJsonNode(toString(object, JsonUtilModule), JsonUtilModule);
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.CAST_FAILURE, e, object.getClass().getName(), "JsonNode");
        }
    }

    /**
     * Object 를 JSON 문자열로 변환
     *
     * @param object
     * @return
     * @throws TeraException
     */
    public static <T> String toString(Object object) throws TeraException {
        return toString(object, JsonUtilModule.NONE);
    }

    /**
     * Object 를 JSON 문자열로 변환
     *
     * @param object
     * @param JsonUtilModule - JsonUtilModule
     * @return
     * @throws TeraException
     */
    public static <T> String toString(Object object, JsonUtilModule JsonUtilModule) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(dateFormat);
            objectMapper.registerModule(new JavaTimeModule());  // LocalDateTime 변환을위한 모듈 등록 (필수)

            switch (JsonUtilModule) {
                case HIBERNATE:
                    objectMapper.registerModule(new Hibernate5Module());
                    break;
                default:
                    break;
            }

            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * JSON 문자열을 Object 로 변환
     *
     * @param json
     * @param clazz
     * @return
     * @throws TeraException
     */
    public static <T> T toObject(String json, Class<T> clazz) throws TeraException {
        T object = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(dateFormat);
            objectMapper.registerModule(new JavaTimeModule());  // LocalDateTime 변환을위한 모듈 등록 (필수)

            object = (T) objectMapper.readValue(json, clazz);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.CAST_FAILURE, e, "Json String", object.getClass().getName());
        }
    }
}
