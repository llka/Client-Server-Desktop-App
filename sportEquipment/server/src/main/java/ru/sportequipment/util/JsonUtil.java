package ru.sportequipment.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.exception.CriticalException;

import java.io.IOException;

public class JsonUtil {
    private static final Logger logger = LogManager.getLogger(JsonUtil.class);

    public static String serialize(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e);
            throw new CriticalException("Cannot create json from " + object);
        }
    }

    public static <T> T deserialize(String json, Class<T> type) throws ApplicationException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error(e);
            throw new ApplicationException("Cannot create object from " + json, ResponseStatus.BAD_REQUEST);
        }
    }

}
