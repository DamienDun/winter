package com.winter.common.utils.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.winter.common.utils.DateUtils;
import com.winter.common.utils.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * LocalDateTime 序列化
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/19 14:25
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> implements ContextualSerializer {

    public final static LocalDateTimeSerializer INSTANCE = new LocalDateTimeSerializer();

    private String format = DateUtils.YYYY_MM_DD_HH_MM_SS;

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formattedDate = DateUtils.parseDateToStr(format, DateUtils.from(value));
        gen.writeString(formattedDate);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (Objects.isNull(property)) {
            return this;
        }
        AnnotationMap anno = property.getMember().getAllAnnotations();
        JsonFormat jsonFormat = anno.get(JsonFormat.class);
        if (jsonFormat != null && StringUtils.isNotEmpty(jsonFormat.pattern())) {
            format = jsonFormat.pattern();
        }
        return this;
    }
}
