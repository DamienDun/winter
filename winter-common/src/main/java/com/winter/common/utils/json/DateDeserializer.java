package com.winter.common.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.winter.common.exception.ServiceException;
import com.winter.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * Date反序列化
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/19 14:13
 */
@Slf4j
public class DateDeserializer extends JsonDeserializer<Date> {

    public final static DateDeserializer INSTANCE = new DateDeserializer();

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            return DateUtils.parseDate(p.getValueAsString(), DateUtils.YYYY_MM_DD_HH_MM_SS);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("字符串解析成日期类型失败");
        }
    }
}
