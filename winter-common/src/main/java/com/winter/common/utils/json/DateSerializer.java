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
import com.winter.common.utils.reflect.ReflectUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;

/**
 * Date序列化
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/19 14:10
 */
public class DateSerializer extends JsonSerializer<Date> implements ContextualSerializer {

    public final static DateSerializer INSTANCE = new DateSerializer();

    /**
     * 格式
     */
    private String pattern;

    public DateSerializer() {
        this.pattern = DateUtils.YYYY_MM_DD_HH_MM_SS;
    }

    public DateSerializer(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (Objects.isNull(gen.getCurrentValue()) || Objects.isNull(gen.getOutputContext())) {
            return;
        }
        String format = pattern;
        // 获取value来源的类
        Class<?> aClass = gen.getCurrentValue().getClass();
        // 获取字段名
        String currentName = gen.getOutputContext().getCurrentName();
        if (StringUtils.isEmpty(currentName)) {
            return;
        }
        Field field = ReflectUtils.getAccessibleFieldByClass(aClass, currentName);
        if (Objects.nonNull(field)) {
            JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
            if (jsonFormat != null && StringUtils.isNotEmpty(jsonFormat.pattern())) {
                format = jsonFormat.pattern();
            }
        }
        if (StringUtils.isEmpty(format)) {
            format = DateUtils.YYYY_MM_DD_HH_MM_SS;
        }
        String formattedDate = DateUtils.parseDateToStr(format, value);
        gen.writeString(formattedDate);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        return this;
    }
}
