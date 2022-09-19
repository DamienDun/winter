package com.winter.common.utils.match;

import com.winter.common.utils.StringUtils;

import java.util.regex.Pattern;

/**
 * 规则匹配
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/19 10:22
 */
public class MatchesUtils {

    /**
     * 手机号正则
     */
    public final static String MOBILE_PHONE = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
    /**
     * 邮箱正则
     */
    public final static String EMAIL = "^([a-z0-9A-Z\u4e00-\u9fa5]+[-|.]?)+[a-z0-9A-Z\u4e00-\u9fa5]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 普通账户正则
     */
    public final static String ACCOUNT = "^[a-zA-Z]\\w{4,16}$";

    /**
     * 数字
     */
    public final static String NUMBER = "[0-9]+";

    /**
     * 字母
     */
    public final static String LETTER = "[a-zA-Z]+";

    /**
     * 中文
     */
    public final static String CHINESE = "[\u4e00-\u9fa5]+";

    /**
     * 数字、字母、中文
     */
    public final static String NUMBER_LETTER_CHINESE = "[a-zA-Z\\u4e00-\\u9fa5][a-zA-Z0-9\\u4e00-\\u9fa5]+";

    /**
     * 时间格式HH:mm:ss
     */
    public final static String HH_MM_SS = "(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";

    /**
     * 是否数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.matches(NUMBER, str);
    }

    /**
     * 是否字母
     *
     * @param str
     * @return
     */
    public static boolean isLetter(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.matches(LETTER, str);
    }

    /**
     * 是否中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.matches(CHINESE, str);
    }

    /**
     * 是否数字、字母或中文
     *
     * @param str
     * @return
     */
    public static boolean isNumberOrLetterOrChinese(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.matches(NUMBER_LETTER_CHINESE, str);
    }

    /**
     * 匹配手机号
     *
     * @param str
     * @return
     */
    public static boolean isMobilePhone(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.matches(MOBILE_PHONE, str);
    }

    /**
     * 匹配邮箱
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.matches(EMAIL, str);
    }

    /**
     * 匹配普通账户格式
     *
     * @param str
     * @return
     */
    public static boolean isAccount(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.matches(ACCOUNT, str);
    }

    /**
     * 匹配时间 HH:mm:ss
     *
     * @param str
     * @return
     */
    public static boolean isHHmmss(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.matches(HH_MM_SS, str);
    }

    /**
     * 匹配身份证格式
     *
     * @param str
     * @return
     */
    public static boolean isIdCard(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return CardNumberUtils.validate(str);
    }

}
