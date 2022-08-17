package com.winter.common.utils;

import com.winter.common.utils.tuple.TupleTwo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;

/**
 * Byte
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/15 21:17
 */
public class ByteUtils {
    /**
     * B 的名称
     */
    public static final String B_NAME = "B";

    /**
     * KB 的名称
     */
    public static final String KB_NAME = "KB";

    /**
     * MB 的名称
     */
    public static final String MB_NAME = "MB";

    /**
     * MB 的名称
     */
    public static final String GB_NAME = "GB";

    /**
     * TB 的名称
     */
    public static final String TB_NAME = "TB";

    /**
     * PB 的名称
     */
    public static final String PB_NAME = "PB";

    /**
     * EB 的名称
     */
    public static final String EB_NAME = "EB";

    /**
     * KB 的字节数
     */
    public static final long KB_SIZE = 1024;
    /**
     * MB 的字节数
     */
    public static final long MB_SIZE = KB_SIZE * KB_SIZE;
    /**
     * GB 的字节数
     */
    public static final long GB_SIZE = MB_SIZE * KB_SIZE;
    /**
     * TB 的字节数
     */
    public static final long TB_SIZE = GB_SIZE * KB_SIZE;
    /**
     * PB 的字节数
     */
    public static final long PB_SIZE = TB_SIZE * KB_SIZE;
    /**
     * EB 的字节数
     */
    public static final long EB_SIZE = PB_SIZE * KB_SIZE;

    /**
     * 获取文件大小
     *
     * @param value 值
     * @return
     */
    public static String getFileSize(Long value) {
        if (value == null || value <= 0) {
            return "0 KB";
        }
        long v = value;
        if (v < KB_SIZE) {
            return String.format("%s B", value);
        } else if (v < MB_SIZE) {
            return calcValue(v, KB_SIZE, "KB");
        } else if (v < GB_SIZE) {
            return calcValue(v, MB_SIZE, "MB");
        } else if (v < TB_SIZE) {
            return calcValue(v, GB_SIZE, "GB");
        } else if (v < EB_SIZE) {
            return calcValue(v, TB_SIZE, "PB");
        } else {
            return calcValue(v, EB_SIZE, "EB");
        }
    }

    /**
     * 获取文件大小和单位
     *
     * @param value
     * @return
     */
    public static TupleTwo<Double, String> getSizeAndUnit(Long value) {
        if (value == null || value <= 0) {
            return new TupleTwo<>(0.0, "KB");
        }
        long v = value;
        if (v < KB_SIZE) {
            return new TupleTwo<>((double) v, "B");
        }
        if (v < MB_SIZE) {
            return calValueAndUnit(value, KB_SIZE, "KB");
        }
        if (v < GB_SIZE) {
            return calValueAndUnit(value, MB_SIZE, "MB");
        }
        if (v < TB_SIZE) {
            return calValueAndUnit(value, GB_SIZE, "GB");
        }
        if (v < EB_SIZE) {
            return calValueAndUnit(value, TB_SIZE, "PB");
        }
        return calValueAndUnit(value, EB_SIZE, "EB");

    }

    /**
     * @param unit
     * @return
     * @author Zhang 根据单位获取单位所对应的字节比例，比如1kb = 1024 b, 1mb = 1024 * 1024 b
     */
    public static long getByteRatio(String unit) {
        unit = unit.toUpperCase().trim();
        if (unit.length() == 1 && !B_NAME.equals(unit)) {
            unit = unit + B_NAME;
        }
        switch (unit) {
            case KB_NAME:
                return KB_SIZE;
            case MB_NAME:
                return MB_SIZE;
            case GB_NAME:
                return GB_SIZE;
            case TB_NAME:
                return TB_SIZE;
            case EB_NAME:
                return EB_SIZE;
            default:
                return 1;
        }
    }

    private static String calcValue(long value, long p, String unit) {
        BigDecimal decimal = new BigDecimal(String.valueOf((double) value / p));
        decimal = decimal.setScale(2, RoundingMode.HALF_EVEN);
        return String.format("%s %s", decimal, unit);
    }

    private static TupleTwo<Double, String> calValueAndUnit(long value, long p, String unit) {
        BigDecimal decimal = new BigDecimal(String.valueOf((double) value / p));
        decimal = decimal.setScale(2, RoundingMode.HALF_EVEN);
        return new TupleTwo<>(Double.valueOf(decimal.toString()), unit);
    }

    /**
     * 是否空白字节
     *
     * @param value 值
     * @return
     */
    public static boolean isWhiteSpace(final byte value) {
        switch (value) {
            case ' ':
            case '\n':
            case '\r':
            case '\t':
                return true;
            default:
                return false;
        }
    }

    /**
     * 获取8位整数二进制的值
     *
     * @param value 值
     * @return
     */
    public static byte[] getRawValue(byte value) {
        byte[] result = new byte[1];
        result[0] = value;
        return result;
    }

    /**
     * 获取二进制的8整数值
     *
     * @param bytes 值
     * @return
     */
    public static byte getByteValue(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return 0;
        }
        return bytes[0];
    }

    /**
     * 获取16位整数二进制的值
     *
     * @param value 值
     * @return
     */
    public static byte[] getRawValue(short value) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(0, value);
        return buffer.array();
    }

    /**
     * 获取二进制的16整数值
     *
     * @param bytes 值
     * @return
     */
    public static short getShortValue(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getShort();
    }

    /**
     * 获取32位整数二进制的值
     *
     * @param value 值
     * @return
     */
    public static byte[] getRawValue(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(0, value);
        return buffer.array();
    }

    /**
     * 获取二进制的32整数值
     *
     * @param bytes 值
     * @return
     */
    public static int getIntegerValue(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * 获取64位整数二进制的值
     *
     * @param value 值
     * @return
     */
    public static byte[] getRawValue(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, value);
        return buffer.array();
    }

    /**
     * 获取二进制的64整数值
     *
     * @param bytes
     * @return
     */
    public static long getLongValue(byte[] bytes) {
        if (bytes == null) {
            return 0L;
        }
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getLong();
    }

    private final static int BIG_INTEGER_LENGTH = 33;

    /**
     * 大数字转换字节流（字节数组）型数据
     *
     * @param n
     * @return
     */
    public static byte[] getRawValue(BigInteger n) {
        if (n == null) {
            return null;
        }
        byte[] tempBytes;
        int len = BIG_INTEGER_LENGTH - 1;
        if (n.toByteArray().length == BIG_INTEGER_LENGTH) {
            tempBytes = new byte[len];
            System.arraycopy(n.toByteArray(), 1, tempBytes, 0, len);
        } else if (n.toByteArray().length == len) {
            tempBytes = n.toByteArray();
        } else {
            tempBytes = new byte[len];
            for (int i = 0; i < len - n.toByteArray().length; i++) {
                tempBytes[i] = 0;
            }
            System.arraycopy(n.toByteArray(), 0, tempBytes, len - n.toByteArray().length, n.toByteArray().length);
        }
        return tempBytes;
    }

    /**
     * 换字节流（字节数组）型数据转大数字
     *
     * @param b
     * @return
     */
    public static BigInteger getBigInteger(byte[] b) {
        if (b[0] < 0) {
            byte[] temp = new byte[b.length + 1];
            temp[0] = 0;
            System.arraycopy(b, 0, temp, 1, b.length);
            return new BigInteger(temp);
        }
        return new BigInteger(b);
    }

    /**
     * 获取 float 二进制的值
     *
     * @param value 值
     * @return
     */
    public static byte[] getRawValue(float value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putFloat(0, value);
        return buffer.array();
    }

    /**
     * 获取二进制的 float 值
     *
     * @param bytes
     * @return
     */
    public static float getFloatValue(byte[] bytes) {
        if (bytes == null) {
            return 0L;
        }
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getFloat();
    }

    /**
     * 获取 double 二进制的值
     *
     * @param value 值
     * @return
     */
    public static byte[] getRawValue(double value) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putDouble(0, value);
        return buffer.array();
    }

    /**
     * 获取二进制的 double 值
     *
     * @param bytes
     * @return
     */
    public static double getDoubleValue(byte[] bytes) {
        if (bytes == null) {
            return 0L;
        }
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getDouble();
    }

    /**
     * 获取 char 二进制的值
     *
     * @param value 值
     * @return
     */
    public static byte[] getRawValue(char value) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putChar(0, value);
        return buffer.array();
    }

    /**
     * 获取二进制的 char 值
     *
     * @param bytes
     * @return
     */
    public static char getCharValue(byte[] bytes) {
        if (bytes == null) {
            return '0';
        }
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getChar();
    }

    /**
     * 根据字节数组获得值(十六进制数字)
     *
     * @param bytes
     * @return
     */
    public static String getHexString(byte[] bytes) {
        return getHexString(bytes, false);
    }

    /**
     * 根据字节数组获得值(十六进制数字)
     *
     * @param bytes
     * @param upperCase 是否转换为大写
     * @return
     */
    public static String getHexString(byte[] bytes, boolean upperCase) {
        StringBuilder ret = new StringBuilder();
        for (byte b : bytes) {
            ret.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return upperCase ? ret.toString().toUpperCase() : ret.toString();
    }

    /**
     * 16进制转换为数组
     *
     * @param hexString 16进制字符窜
     * @param upperCase 是否转换为大写
     * @return byte[] 返回数组
     */
    public static byte[] hexStringToBytes(String hexString, boolean upperCase) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        if (upperCase) {
            hexString = hexString.toUpperCase();
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToHexByte(hexChars[pos], upperCase) << 4 | charToHexByte(hexChars[pos + 1], upperCase));
        }
        return d;
    }

    /**
     * 16进制字符大写
     */
    public final static String HEXDIGITS_STRING_UPPER = "0123456789ABCDEF";

    /**
     * 16进制字符小写
     */
    public final static String HEXDIGITS_STRING_LOWER = "0123456789abcdef";

    /**
     * 将字符转换为16进制的字节
     *
     * @param c char 字符
     * @return byte
     */
    public static byte charToHexByte(char c) {
        return charToHexByte(c, false);
    }

    /**
     * 将字符转换为16进制的字节
     *
     * @param c         字符
     * @param upperCase 是否为大写
     * @return byte
     */
    public static byte charToHexByte(char c, boolean upperCase) {
        if (upperCase) {
            return (byte) HEXDIGITS_STRING_UPPER.indexOf(c);
        }
        return (byte) HEXDIGITS_STRING_LOWER.indexOf(c);
    }
}
