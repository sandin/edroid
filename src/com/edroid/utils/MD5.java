package com.edroid.utils;

import java.security.MessageDigest;



public class MD5 {
	
	/**
	 * md5算法加密，得到32位密文
	 * @param plainText
	 * @return 密文
	 */
	public static String md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("utf8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * md5算法加密，将得到的32位密文中的数字去掉
	 * @param plainText
	 * @return 密文
	 */
	public static String md5en(String str) {
		String md = md5(str);
		String rst = "";
		for (int i = 0; i < md.length(); i++) {
			if(md.charAt(i) < 48 || md.charAt(i) > 57){
				rst += md.charAt(i);
			}
		}
		return rst;
	}
}