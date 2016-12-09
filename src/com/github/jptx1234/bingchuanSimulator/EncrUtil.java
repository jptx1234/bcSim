package com.github.jptx1234.bingchuanSimulator;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

class EncrUtil {
	private static final int RADIX = 16;
	private static final String SEED = "4893249324432943493499";

	public static final String encrypt(char[] password) {
		if (password == null) {
			return "";
		}
		if (password.length == 0) {
			return "";
		}
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(password.length);
		cb.put(password);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		Arrays.fill(password, '0');
		byte[] passBytes = bb.array();

		BigInteger bi_passwd = new BigInteger(passBytes);
		Arrays.fill(passBytes, (byte) 0);

		BigInteger bi_r0 = new BigInteger(SEED);
		BigInteger bi_r1 = bi_r0.xor(bi_passwd);
		return bi_r1.toString(RADIX);
	}

	public static final String decrypt(String encrypted) {
		if (encrypted == null) {
			return "";
		}
		if (encrypted.length() == 0) {
			return "";
		}

		BigInteger bi_confuse = new BigInteger(SEED);

		try {
			BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
			BigInteger bi_r0 = bi_r1.xor(bi_confuse);

			return new String(bi_r0.toByteArray());
		} catch (Exception e) {
			return "";
		}
	}
}
