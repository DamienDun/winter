package com.winter.common.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.winter.common.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * LocalDateTime 序列化
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/19 14:25
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    public final static LocalDateTimeSerializer INSTANCE = new LocalDateTimeSerializer();

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formattedDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.from(value));
        gen.writeString(formattedDate);
    }
}
