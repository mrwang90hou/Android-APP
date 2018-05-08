package com.diary.check;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * 文本工具类
 * 
 * @author ty
 *
 */
public class TextUtil {

	/**
	 * 得到文件二进制流
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static byte[] getFileContent(String fileName) throws Exception {
		File file = new File(fileName);
		if (file.exists()) {
			InputStream is = new FileInputStream(file);
			return getFileByte(is);
		}
		return null;
	}

	/**
	 * 输入流转byte数组
	 * 
	 * @param is
	 * @return
	 */
	public static byte[] getFileByte(InputStream is) {
		try {
			byte[] buffer = new byte[1024];
			int len = -1;
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			byte[] data = outStream.toByteArray();
			outStream.close();
			is.close();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串不能空
	 * 
	 * @param value
	 *            要验证的字符串
	 * @return
	 */
	public static boolean stringIsNotNull(String value) {
		return value != null && value.trim().length() > 0;
	}

	/**
	 * 字符串为空
	 * 
	 * @param value
	 *            要验证的字符串
	 * @return
	 */
	public static boolean stringIsNull(String value) {
		return value == null || value.trim().length() <= 0;
	}

	/**
	 * 
	 * @author ty
	 * @createdate 2012-6-26 下午10:38:11
	 * @Description: 字符串转InputStream
	 * @param xml
	 * @return
	 */
	public static InputStream stringToInputStream(String xml) {
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		return stream;
	}

	/**
	 * 获取文本内容的长度，中文算一个字符，英文算半个字符，包括标点符号
	 * 
	 * @param str
	 * @return
	 */
	public static int getTextLengthes(String str) {
		int number = getTextLength(str);
		int length = number / 2;
		if (number % 2 != 0) {
			length += 1;
		}
		str = null;
		return length;
	}

	/**
	 * 获取文本内容的长度(中文算两个字符，英文算一个字符)
	 * 
	 * @param str
	 * @return
	 */
	public static int getTextLength(String str) {
		int length = 0;
		try {
			str = new String(str.getBytes("GBK"), "ISO8859_1");
			length = str.length();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return length;
	}

	/**
	 * 
	 * @autor bo.wang
	 * @createdate 2012-11-28 上午10:14:47
	 * @Description 酒店价格转换 1，100.1转换为101 2，100.000装换为100
	 * @param price
	 *            需要转换的价格
	 * 
	 */
	public static String formatPrice(String price) {
		if (price.contains(".")) {
			String price2 = String.valueOf(price).substring(price.lastIndexOf(".") + 1, price.length());
			if (Integer.valueOf(price2) > 0) {
				String price3 = String.valueOf(Integer.valueOf(price.substring(0, price.lastIndexOf("."))) + 1);
				return price3;

			} else {
				String price4 = price.substring(0, price.lastIndexOf("."));
				return price4;
			}
		} else {
			return price;
		}
	}

	/**
	 * 
	 * @author lk
	 * @createdate 2014-9-30 上午11:28:14
	 * @Description: (数字、字母及标点全部转为全角字符)
	 * @param input
	 * @return
	 *
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 
	 * @author LK
	 * @methods isZeroOrNull
	 * @description 描述信息 判断参数是否为零或者为空
	 * @date 2014-12-4 下午7:10:35
	 * @return 参数说明
	 */
	public static boolean isZeroOrNull(String result) {
		if (TextUtils.isEmpty(result) || "0".equals(result) || "0.0".equals(result) || "0.00".equals(result)) {
			return true;
		}
		return false;
	}

	/**
	 * 实现文本复制功能
	 * 
	 * @param content
	 *            要复制的内容
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void copy(String content, Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}

	/**
	 * @param
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5(String val) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(val.getBytes());
		byte[] byteStr = md5.digest();// 加密
		StringBuffer sb = new StringBuffer();
		int bytelength = byteStr.length;
		for (int i = 0; i < bytelength; i++) {
			sb.append(byteStr[i]);
		}
		return sb.toString();
	}

    /**
     *  wxy 2016年1月9日17:10:52 检查集合是否为空
     * @param arrayList
     * @return
     */
    public static boolean arrayListIsNotNull(ArrayList<Object> arrayList) {
        if (null != arrayList && arrayList.size() > 0) {
            return true;
        }
        return false;
    }
}
