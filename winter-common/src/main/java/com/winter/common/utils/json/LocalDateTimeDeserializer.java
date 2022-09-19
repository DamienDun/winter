package com.winter.common.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.winter.common.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * LocalDateTime 反序列化
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/19 14:31
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    public final static LocalDateTimeDeserializer INSTANCE = new LocalDateTimeDeserializer();

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return DateUtils.parseLocalDateTime(p.getValueAsString());
    }
}
