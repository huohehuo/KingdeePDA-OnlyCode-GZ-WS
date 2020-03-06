package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private static byte[] m;

	public static String getMD5(String val){
		MessageDigest md5 = null;
		if(val!=null){
			try {
				md5 = MessageDigest.getInstance("MD5");
				md5.update(val.getBytes());
				//加密
				m = md5.digest();

			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return getString(m);
		}
		return getString(m);
	}
	private static String getString(byte[] b) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int a = b[i];
			if (a < 0)
				a += 256;
			if (a < 16)
				buf.append("0");
			buf.append(Integer.toHexString(a));

		}
		return buf.toString();
//		return buf.toString().substring(8, 24);  //32位
	}


	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr){
		MessageDigest md5 = null;
		try{
			md5 = MessageDigest.getInstance("MD5");
		}catch (Exception e){
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++){
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public static String convertMD5(String inStr){

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++){
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}

}
