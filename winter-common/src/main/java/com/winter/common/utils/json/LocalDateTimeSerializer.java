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

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String format = DateUtils.YYYY_MM_DD_HH_MM_SS;
        // 获取value来源的类
        Class<?> aClass = gen.getCurrentValue().getClass();
        // 获取字段名
        String currentName = gen.getOutputContext().getCurrentName();
        List<Field> fieldList = new ArrayList<>();
        fieldList.addAll(Arrays.asList(aClass.getFields()));
        fieldList.addAll(Arrays.asList(aClass.getDeclaredFields()));
        for (Field field : fieldList) {
            if (field.getName().equals(currentName)) {
                JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
                if (jsonFormat != null && StringUtils.isNotEmpty(jsonFormat.pattern())) {
                    format = jsonFormat.pattern();
                }
            }
        }
        String formattedDate = DateUtils.parseDateToStr(format, DateUtils.from(value));
        gen.writeString(formattedDate);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        return this;
    }
}
