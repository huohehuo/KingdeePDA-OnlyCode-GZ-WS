package com.fangzuo.assist.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	//大写
	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

//小写
//    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
	//            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

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
		return buf.toString().substring(8, 24);  //32位
	}
	/**
	 * 32位MD5加密的大写字符串
	 *
	 * @param s
	 * @return
	 */
	public final static String getMD5to32Big(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	// 返回形式为数字跟字符串
	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		// System.out.println("iRet="+iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	// 返回形式只为数字
	private static String byteToNum(byte bByte) {
		int iRet = bByte;
		System.out.println("iRet1=" + iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		return String.valueOf(iRet);
	}

	// 转换字节数组为16进制字串
	private static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	public static String GetMD5Code(String strObj) {
		String resultString = null;
		try {
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.digest() 该函数返回值为存放哈希值结果的byte数组
			resultString = byteToString(md.digest(strObj.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return resultString;
	}

	/*public static void main(String[] args) {
		MD5 getMD5 = new MD5();
		System.out.println("MD5的结果："+getMD5.GetMD5Code("qweerretw%$12sde"));
	}
}*/

	/*（第二种）*/
	public static String stringMD5(String pw) {
		try {
			MessageDigest messageDigest =MessageDigest.getInstance("MD5");
			byte[] inputByteArray = pw.getBytes();
			messageDigest.update(inputByteArray);
			byte[] resultByteArray = messageDigest.digest();
			return byteArrayToHex(resultByteArray);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static String byteArrayToHex(byte[] byteArray) {

		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f' };
		char[] resultCharArray =new char[byteArray.length * 2];
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b& 0xf];
		}
		return new String(resultCharArray);
	}
}
