package com.winter.common.utils.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.winter.common.utils.DateUtils;
import com.winter.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
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
@Slf4j
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> implements ContextualSerializer {

    public final static LocalDateTimeSerializer INSTANCE = new LocalDateTimeSerializer();

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String format = DateUtils.YYYY_MM_DD_HH_MM_SS;
        // 获取value来源的类
        Class<?> aClass = gen.getCurrentValue().getClass();
        // 获取字段名
        String currentName = gen.getOutputContext().getCurrentName();
        try {
            // 获取字段
            Field declaredField = aClass.getDeclaredField(currentName);
            JsonFormat jsonFormat = declaredField.getAnnotation(JsonFormat.class);
            if (jsonFormat != null && StringUtils.isNotEmpty(jsonFormat.pattern())) {
                format = jsonFormat.pattern();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        String formattedDate = DateUtils.parseDateToStr(format, DateUtils.from(value));
        gen.writeString(formattedDate);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (Objects.isNull(property)) {
            return this;
        }
        return this;
    }
}
