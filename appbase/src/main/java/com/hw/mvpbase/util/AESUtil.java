package com.hw.mvpbase.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具
 * 采用AES/ECB/PKCS5Padding，128位加密
 *
 * @author Saintcy
 */
public class AESUtil {
    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    public static byte[] encrypt(String content, String password) {
        try {
            //这里是128bit密钥，所以是 16 byte,如果是其它长度的 除以8 ，修改，这里简单演示一下
            byte[] newkey = new byte[16];
            for (int i = 0; i < newkey.length && i < password.getBytes().length; i++) {
                newkey[i] = password.getBytes()[i];
            }

            SecretKeySpec key = new SecretKeySpec(newkey, "AES");
            // 创建密码器，AES默认是/ECB/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] byteContent = content.getBytes("utf-8");
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密成16进制字符串
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    public static String encryptToHex(String content, String password) {
        return parseByte2HexStr(encrypt(content, password));
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static String decrypt(byte[] content, String password) {
        try {
            //这里是128bit密钥，所以是 16 byte,如果是其它长度的 除以8 ，修改，这里简单演示一下
            byte[] newkey = new byte[16];
            for (int i = 0; i < newkey.length && i < password.getBytes().length; i++) {
                newkey[i] = password.getBytes()[i];
            }

            SecretKeySpec key = new SecretKeySpec(newkey, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            // 加密
            return new String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从16进制内容中解密
     *
     * @param hexContent 16进制的加密内容
     * @param password   密码
     * @return
     */
    public static String decryptFromHex(String hexContent, String password) {
        return decrypt(parseHexStr2Byte(hexContent), password);
    }


    public static String decryptFromBase64(String hexContent, String password) {
        return decrypt(Base64.decode(hexContent, Base64.NO_WRAP), password);
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
