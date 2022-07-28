package com.demo.demo.controller.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @Author chaoqun.jiang
 * @create 2021/4/22 11:28
 */
public class FileUtils {

    /**
     * java计算文件32位md5值
     * @param fis 输入流
     * @return
     */
    public static String md5HashCode32(InputStream fis) {
        try {
            //拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest md = MessageDigest.getInstance("MD5");
            return getHashCode(fis, md);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * java计算文件SHA-1哈希值
     * @param fis 输入流
     * @return
     */
    public static String sha1HashCode(InputStream fis) {
        try {
            //拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return getHashCode(fis, md);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String md5HashCode32(String input) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());

            //拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest md = MessageDigest.getInstance("MD5");
            return getHashCode(bais, md);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getHashCode(InputStream fis, MessageDigest md) throws IOException {
        //分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少。
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = fis.read(buffer, 0, 1024)) != -1) {
            md.update(buffer, 0, length);
        }
        fis.close();

        //转换并返回包含16个元素字节数组,返回数值范围为-128到127
        byte[] md5Bytes  = md.digest();
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                /**
                 * 如果小于16，那么val值的16进制形式必然为一位，
                 * 因为十进制0,1...9,10,11,12,13,14,15 对应的 16进制为 0,1...9,a,b,c,d,e,f;
                 * 此处高位补0。
                 */
                hexValue.append("0");
            }
            //这里借助了Integer类的方法实现16进制的转换
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

}
