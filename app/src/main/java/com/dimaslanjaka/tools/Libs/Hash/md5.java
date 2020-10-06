package com.dimaslanjaka.tools.Libs.Hash;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class md5 {
	public static String encode(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.trim().getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : array) {
				sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString().trim();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5 + "-md5-error";
	}
}
