package com.xiaomi.ibook.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @author xiaomi
 * create by 2020/04/21 14:59
 */
@Component
@Slf4j
public class AESUtil {

    private static final String ENCODE = StandardCharsets.UTF_8.name();

    private static final String ENCRYPT_TYPE = "AES";

    /**
     * AES加密字符串
     *
     * @param mingwen 需要被加密的字符串
     * @param password  加密需要的密码
     * @return 密文
     */
    public String encrypt(String mingwen, String password) {
        try {
            SecretKeySpec key = getSecretKeySpec(password);
            Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);// 创建密码器
            byte[] byteContent = mingwen.getBytes(ENCODE);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return parseByte2HexStr(result);
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }
    /**
     * 解密AES加密过的字符串
     *
     * @param miwen AES加密过过的内容
     * @param password  加密时的密码
     * @return 明文
     */
    public String decrypt(String miwen, String password) {
        try {
            byte[] byte2 = parseHexStr2Byte(miwen);
            SecretKeySpec key = getSecretKeySpec(password);
            Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(byte2);
            return new String(result,ENCODE);
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }

    private SecretKeySpec getSecretKeySpec(String password) throws Exception{
        KeyGenerator kgen = KeyGenerator.getInstance(ENCRYPT_TYPE);// 创建AES的Key生产者
        kgen.init(128, new SecureRandom(password.getBytes()));
        SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
        byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
        return new SecretKeySpec(enCodeFormat, ENCRYPT_TYPE);// 转换为AES专用密钥
    }

    /**将二进制转换成16进制
     * @param buf 2进制的字节数组
     * @return 16进制的字符串
     */
    private String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexStr 16进制的字符串
     * @return 2进制的字节数组
     */
    private byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) {
        AESUtil aesUtil = new AESUtil();
        String mingwen = "";
        String password = "123456";
        String miwen = aesUtil.encrypt(mingwen,password);
        System.out.println("加密后:" + miwen);
    }


}