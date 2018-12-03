package ru.sportequipment.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.client.exception.ClientException;

import java.io.IOException;
import java.util.List;

public class JsonUtil {
    private static final Logger logger = LogManager.getLogger(JsonUtil.class);

    public static String serialize(Object object) throws ClientException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e);
            throw new ClientException("Cannot create json from " + object);
        }
    }

    public static <T> T deserialize(String json, Class<T> type) throws ClientException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error(e);
            throw new ClientException("Cannot create object from " + json);
        }
    }

    public static <T> T deserialize(String json, TypeReference<T> type) throws ClientException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error(e);
            throw new ClientException("Cannot create object from " + json);
        }
    }


    public static <T> List<T> deserializeList(String json, T type) throws ClientException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        try {
            return mapper.readValue(json, new TypeReference<List<T>>() {
            });
        } catch (IOException e) {
            logger.error(e);
            throw new ClientException("Cannot create object from " + json);
        }
    }


}
