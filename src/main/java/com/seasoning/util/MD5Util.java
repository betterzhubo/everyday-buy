package com.seasoning.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MD5Util {
	
	private static final Logger logger = LogManager.getLogger(MD5Util.class);
	
	public static final String MD5="MD5";
    public static final String UTF8="UTF-8";

    /**
     *  采用加密算法加密字符串数据 转成长度为32的字符串
     * @param str 需要加密的字符
     * @return 加密后的数据
     */
    public static String EncryptionStr32(String str) {
        // 加密之后所得字节数组
        byte[] bytes = EncryptionStrBytes(str);
        return BytesConvertToHexString(bytes);
    }


    /**
     *  采用加密算法加密字符串数据  转成长度为16的字符串
     * @param str 需要加密的字符
     * @return 加密后的数据
     */
    public static String EncryptionStr16(String str) {
        return EncryptionStr32(str).substring(8,24);
    }


    /**
     * 采用加密算法加密字符串数据
     * @param str   需要加密的数据
     * @return 字节数据
     */
    public static byte[] EncryptionStrBytes(String str) {
        // 加密之后所得字节数组
        byte[] bytes = null;
        try {
            // 获取MD5算法实例 得到一个md5的消息摘要
            MessageDigest md = MessageDigest.getInstance(MD5);
            
            //得到该摘要
            bytes = md.digest(str.getBytes(UTF8));
        } catch (NoSuchAlgorithmException e) {
        	logger.error("MD5加密算法不存在");
        } catch (UnsupportedEncodingException e) {
        	logger.error("数据加密指定的utf8编码格式不支持");
        }
        return null==bytes?null:bytes;
    }
    /**
     * 把字节数组转化成字符串返回
     * @param bytes 加密数组
     * @return 加密后的数据
     */
    public static String BytesConvertToHexString(byte [] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes) {
            String s=Integer.toHexString(0xff & aByte);
            if(s.length()==1){
                sb.append("0"+s);
            }else{
                sb.append(s);
            }
        }
        return sb.toString();
    }

    //测试上述方法
    public static void main(String[] args) {
        String test1="admin";
        String test2="QWERFVDSCX";
        String test3="23423KJHkdfg";
        String [] test={test1,test2,test3};
        for (String s : test) {
            String str=EncryptionStr32(s);
            System.out.println("数据：" + s+" 加密之后的结果为："+str+" 字符串长度为："+str.length());
            str = EncryptionStr16(s);
            System.out.println("数据：" + s+" 加密之后的结果为："+str+" 字符串长度为："+str.length());
        }
    }
}
