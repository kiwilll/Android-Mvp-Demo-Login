package com.hw.mvpbase.util;

/**
 * Created by hasee on 2018/4/25.
 */

public class HexUtil {

    private final static String HEX = "0123456789abcdef";
    private final static String HEXUP = "0123456789ABCDEF";

    public static String toHex(String txt) {
        if (txt == null)
            return null;
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        if (hex == null)
            return null;
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        if (hexString == null)
            return null;
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return null;
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    public static String toHexUp(byte[] buf) {
        if (buf == null)
            return null;
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHexUp(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHexUp(StringBuffer sb, byte b) {
        sb.append(HEXUP.charAt((b >> 4) & 0x0f)).append(HEXUP.charAt(b & 0x0f));
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

}
