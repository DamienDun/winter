package com.winter.common.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.winter.common.utils.DateUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Date序列化
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/19 14:10
 */
public class DateSerializer extends JsonSerializer<Date> {

    public final static DateSerializer INSTANCE = new DateSerializer();

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formattedDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, value);
        gen.writeString(formattedDate);
    }
}
