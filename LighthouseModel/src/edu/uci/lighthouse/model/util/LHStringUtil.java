package edu.uci.lighthouse.model.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LHStringUtil {

	public static String getMD5Hash(String s) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		byte[] data = s.getBytes();
		m.update(data, 0, data.length);
		BigInteger i = new BigInteger(1, m.digest());
		return String.format("%1$032X", i);
	}
	
}
