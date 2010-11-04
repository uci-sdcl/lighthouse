package edu.uci.lighthouse.model.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

public class LHStringUtil {

	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String getMD5Hash(String s) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		byte[] data = s.getBytes();
		m.update(data, 0, data.length);
		BigInteger i = new BigInteger(1, m.digest());
		return String.format("%1$032X", i);
	}
	
}
