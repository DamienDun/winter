package com.winter.common.factory;

import com.winter.common.annotation.Trimmed;
import com.winter.common.annotation.Trimmed.TrimmerType;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Trimmed注解格式处理工厂
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/9/7 8:56
 */
public class TrimmedAnnotationFormatterFactory implements AnnotationFormatterFactory<Trimmed> {

    public static final Pattern PATTERN_WHITESPACES = Pattern.compile("\\s+");
    public static final Pattern PATTERN_WHITESPACES_WITH_LINE_BREAK = Pattern.compile("\\s*\\n\\s*");
    public static final Pattern PATTERN_WHITESPACES_EXCEPT_LINE_BREAK = Pattern.compile("[\\s&&[^\\n]]+");

    private static final Map<TrimmerType, TrimmerFormatter> TRIMMER_FORMATTER_MAP;

    static {
        TrimmerType[] values = TrimmerType.values();
        Map<TrimmerType, TrimmerFormatter> map = new HashMap<>(values.length);
        for (TrimmerType type : values) {
            map.put(type, new TrimmerFormatter(type));
        }
        TRIMMER_FORMATTER_MAP = Collections.unmodifiableMap(map);
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        Set<Class<?>> fieldTypes = new HashSet<>(1, 1);
        fieldTypes.add(String.class);
        return fieldTypes;
    }

    @Override
    public Parser<?> getParser(Trimmed annotation, Class<?> fieldType) {
        return TRIMMER_FORMATTER_MAP.get(annotation.value());
    }

    @Override
    public Printer<?> getPrinter(Trimmed annotation, Class<?> fieldType) {
        return TRIMMER_FORMATTER_MAP.get(annotation.value());
    }

    private static class TrimmerFormatter implements Formatter<String> {

        private final TrimmerType type;

        public TrimmerFormatter(TrimmerType type) {
            if (type == null) {
                throw new NullPointerException();
            }
            this.type = type;
        }

        @Override
        public String print(String object, Locale locale) {
            return object;
        }

        @Override
        public String parse(String text, Locale locale) throws ParseException {
            return trimString(text, type);
        }
    }

    /**
     * 清除String空格方法
     *
     * @param text
     * @param type
     * @return
     */
    public static String trimString(String text, TrimmerType type) {
        if (Objects.isNull(text)) {
            return null;
        }
        text = text.trim();
        switch (type) {
            case ALL_WHITESPACES:
                return PATTERN_WHITESPACES.matcher(text).replaceAll("");
            case EXCEPT_LINE_BREAK:
                return PATTERN_WHITESPACES_EXCEPT_LINE_BREAK
                        .matcher(PATTERN_WHITESPACES_WITH_LINE_BREAK.matcher(text).replaceAll("\n")).replaceAll("");
            case SIMPLE:
                return text;
            default:
                // not possible
                throw new AssertionError();
        }
    }
}
