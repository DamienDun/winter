package com.winter.common.utils.match;

import com.winter.common.exception.ServiceException;
import com.winter.common.utils.DateUtils;
import com.winter.common.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 身份证号工具
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/9/19 10:23
 */
public class CardNumberUtils {

    /**
     * 身份证号码中的出生日期的格式
     */
    private final static String BIRTH_DATE_FORMAT = "yyyyMMdd";

    /**
     * 身份证的最小出生日期,1900年1月1日
     */
    private final static Date MINIMAL_BIRTH_DATE = new Date(-2209017600000L);

    private final static int NEW_CARD_NUMBER_LENGTH = 18;

    private final static int OLD_CARD_NUMBER_LENGTH = 15;

    // 18位身份证中最后一位校验码
    private final static char[] VERIFY_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    // 18位身份证中，各个数字的生成校验码时的权值
    private final static int[] VERIFY_CODE_WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 如果是15位身份证号码，则自动转换为18位
     *
     * @param cardNumber
     * @return
     */
    public static boolean check(String cardNumber) {
        if (null != cardNumber) {
            cardNumber = cardNumber.trim();
            if (OLD_CARD_NUMBER_LENGTH == cardNumber.length()) {
                cardNumber = contertToNewCardNumber(cardNumber);
            }
            return validate(cardNumber);
        }
        return false;
    }

    /**
     * 获取完整身份证号
     *
     * @param cardNumber 身份证号
     * @return
     */
    public static String getFullCardNumber(String cardNumber) {
        if (StringUtils.isEmpty(cardNumber)) {
            return "";
        }
        cardNumber = cardNumber.trim();
        if (cardNumber.length() == 15) {
            cardNumber = contertToNewCardNumber(cardNumber);
        }
        if (!validate(cardNumber)) {
            throw new ServiceException("无效的身份证号,无法解析日期。");
        }
        return cardNumber;
    }

    /**
     * 获取身份证出生日期
     *
     * @param cardNumber 身份证号
     * @return 返回格式为 yyyy-MM-dd 的出生日期
     */
    public static String getDateOfBirthString(String cardNumber) {
        cardNumber = getFullCardNumber(cardNumber);
        if (cardNumber.length() == 0) {
            return "";
        }
        return cardNumber.substring(6, 10) + "-" + cardNumber.substring(10, 12) + "-" + cardNumber.substring(12, 14);
    }

    /**
     * 获取出生日期
     *
     * @param cardNumber
     * @return
     */
    public static Date getDateOfBirth(String cardNumber) {
        String birth = getDateOfBirthString(cardNumber);
        if (birth.length() == 0) {
            return null;
        }
        try {
            return new SimpleDateFormat(DateUtils.YYYY_MM_DD)
                    .parse(birth);
        } catch (ParseException e) {
            throw new ServiceException("无效的身份证号,无法解析日期。");
        }
    }

    /**
     * 获取出生日期，返回本地日期
     *
     * @param cardNumber
     * @return
     */
    public static LocalDate getLocalDateOfBirth(String cardNumber) {
        String birth = getDateOfBirthString(cardNumber);
        if (birth.length() == 0) {
            return null;
        }
        return LocalDate.parse(birth, DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD));
    }

    /**
     * 身份证验证
     *
     * @param cardNumber
     * @return
     */
    public static boolean validate(String cardNumber) {
        // 身份证号不能为空
        boolean result = cardNumber != null;
        if (!result) {
            return false;
        }
        // 身份证号长度是18(新证)
        result = NEW_CARD_NUMBER_LENGTH == cardNumber.length();
        if (!result) {
            return false;
        }
        // 身份证号的前17位必须是阿拉伯数字
        for (int i = 0; i < NEW_CARD_NUMBER_LENGTH - 1; i++) {
            char ch = cardNumber.charAt(i);
            result = ch >= '0' && ch <= '9';
            if (!result) {
                return false;
            }
        }
        // 身份证号的第18位校验正确
        result = calculateVerifyCode(cardNumber) == cardNumber
                .charAt(NEW_CARD_NUMBER_LENGTH - 1);
        if (!result) {
            return false;
        }
        // 出生日期不能晚于当前时间，并且不能早于1900年
        try {
            Date birthDate = new SimpleDateFormat(BIRTH_DATE_FORMAT)
                    .parse(getBirthDayPart(cardNumber));
            result = birthDate != null;
            result = result && birthDate.before(new Date());
            result = result && birthDate.after(MINIMAL_BIRTH_DATE);
            if (!result) {
                return false;
            }
            /**
             * 出生日期中的年、月、日必须正确,比如月份范围是[1,12],
             * 日期范围是[1,31]，还需要校验闰年、大月、小月的情况时，
             * 月份和日期相符合
             */
            String birthdayPart = getBirthDayPart(cardNumber);
            String realBirthdayPart = new SimpleDateFormat(BIRTH_DATE_FORMAT)
                    .format(birthDate);
            result = birthdayPart.equals(realBirthdayPart);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    private static String getBirthDayPart(String cardNumber) {
        return cardNumber.substring(6, 14);
    }

    /**
     * 校验码（第十八位数）：
     * <p>
     * 十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0...16 ，先对前17位数字的权求和；
     * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
     * 2; 计算模 Y = mod(S, 11)< 通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9
     * 8 7 6 5 4 3 2
     *
     * @param cardNumber
     * @return
     */
    private static char calculateVerifyCode(CharSequence cardNumber) {
        int sum = 0;
        for (int i = 0; i < NEW_CARD_NUMBER_LENGTH - 1; i++) {
            char ch = cardNumber.charAt(i);
            sum += ((int) (ch - '0')) * VERIFY_CODE_WEIGHT[i];
        }
        return VERIFY_CODE[sum % 11];
    }

    /**
     * 把15位身份证号码转换到18位身份证号码<br>
     * 15位身份证号码与18位身份证号码的区别为：<br>
     * 1、15位身份证号码中，"出生年份"字段是2位，转换时需要补入"19"，表示20世纪<br>
     * 2、15位身份证无最后一位校验码。18位身份证中，校验码根据根据前17位生成
     *
     * @param oldCardNumber
     * @return
     */
    private static String contertToNewCardNumber(String oldCardNumber) {
        StringBuilder buf = new StringBuilder(NEW_CARD_NUMBER_LENGTH);
        buf.append(oldCardNumber.substring(0, 6));
        buf.append("19");
        buf.append(oldCardNumber.substring(6));
        buf.append(CardNumberUtils.calculateVerifyCode(buf));
        return buf.toString();
    }
}
