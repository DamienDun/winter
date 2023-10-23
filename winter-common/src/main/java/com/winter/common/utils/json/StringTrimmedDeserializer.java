package com.winter.common.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.winter.common.annotation.Trimmed;
import com.winter.common.factory.TrimmedAnnotationFormatterFactory;
import com.winter.common.utils.reflect.ReflectUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 字符串去空 反序列化
 * 对post请求 json请求体有效
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/10/23 14:04
 */
public class StringTrimmedDeserializer extends JsonDeserializer<String> {

    public final static StringTrimmedDeserializer INSTANCE = new StringTrimmedDeserializer();

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // 获取value来源的类
        Class<?> aClass = p.getCurrentValue().getClass();
        // 获取字段名
        String currentName = p.getCurrentName();
        Field field = ReflectUtils.getAccessibleFieldByClass(aClass, currentName);
        if (Objects.nonNull(field)) {
            Trimmed trimmed = field.getAnnotation(Trimmed.class);
            if (Objects.nonNull(trimmed)) {
                return TrimmedAnnotationFormatterFactory.trimString(p.getValueAsString(), trimmed.value());
            }
        }
        return p.getValueAsString();
    }
}
