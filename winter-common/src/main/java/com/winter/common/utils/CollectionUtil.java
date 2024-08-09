package com.winter.common.utils;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 集合工具类
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/13 8:40
 */
public class CollectionUtil extends CollectionUtils {

    /**
     * 判断集合不为空,过滤null元素
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return false;
        }
        for (Object o : collection) {
            if (o == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * stream 根据Map 指定的某个key对应的值 去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * stream 根据Map 指定的某个key对应的值 获重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> getRepeatByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) != null;
    }

    /**
     * 判断字符串是否包含List中的任一个元素
     *
     * @param str
     * @param strList
     * @return
     */
    public static boolean strContainsList(String str, List<String> strList) {
        for (String e : strList) {
            if (str.contains(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在list集合中随机取出指定数量的元素
     *
     * @param dest  取元素的集合
     * @param count 个数
     * @return
     */
    public static <T> List<T> randomList(List<T> dest, int count) {
        if (CollectionUtils.isEmpty(dest)) {
            return Collections.emptyList();
        }
        Collections.shuffle(dest);
        if (dest.size() <= count) {
            return dest;
        }
        return dest.subList(0, count);
    }
}
