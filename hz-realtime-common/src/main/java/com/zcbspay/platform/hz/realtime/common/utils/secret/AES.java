package com.zcbspay.platform.hz.realtime.common.utils.secret;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(String content, String password) {
		try {
			// KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// kgen.init(128, new SecureRandom(password.getBytes()));
			// SecretKey secretKey = kgen.generateKey();
			// byte[] enCodeFormat = secretKey.getEncoded();
			
			byte [] bb = parseHexStr2Byte(password);
			
//			for (int i =0; i<bb.length; i++)
//			{
//				System.out.printf("=====================%d\n",bb[i] &0xff);
//			}

			SecretKeySpec key = new SecretKeySpec(parseHexStr2Byte(password), "AES");
			// Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] byteContent = content.getBytes("utf-8");

			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password) {
		try {
			// KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// kgen.init(128, new SecureRandom(password.getBytes()));
			// SecretKey secretKey = kgen.generateKey();
			// byte[] enCodeFormat = secretKey.getEncoded();

			SecretKeySpec key = new SecretKeySpec(parseHexStr2Byte(password), "AES");

			// Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt2(String content, String password) {
		try {
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		String content = "test";
		String password = "12345678";
		// 加密
		System.out.println("加密前：" + content);
		byte[] encryptResult = encrypt(content, password);
		String tt4 = Base64Utils.encode(encryptResult);
		System.out.println(new String(tt4));

		// 解密
		byte[] decryptResult = decrypt(encryptResult, password);
		System.out.println("解密后：" + new String(decryptResult));

		final SecureRandom random = new SecureRandom(new String("123455333333").getBytes());
		// final SecureRandom random = new SecureRandom();
		final KeyGenerator generate;

		generate = KeyGenerator.getInstance("AES");

		generate.init(192, random);
		// System.out.println(new String(generate.generateKey().getEncoded()));
		PrintUtils.printHex(generate.generateKey().getEncoded());

		SecureRandom random2 = null;

		byte[] b = new byte[192];
		for (int i = 0; i < 10; i++) {
			random2 = SecureRandom.getInstance("SHA1PRNG");
			random2.nextBytes(b);

			random2.setSeed("123455333333".getBytes());
			generate.init(192, random2);
			// System.out.println(new
			// String(generate.generateKey().getEncoded()));
			PrintUtils.printHex(generate.generateKey().getEncoded());

		}

	}
	
	public static String generateKey() {
		String s = null;
		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(128);// 要生成多少位，只需要修改这里即可128, 192或256
			SecretKey sk = kg.generateKey();
			byte[] b = sk.getEncoded();
			s = parseByte2HexStr(b);
			System.out.println(s);
			System.out.println("十六进制密钥长度为" + s.length());
			System.out.println("二进制密钥的长度为" + s.length() * 4);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("没有此算法。");
		}
		return s;
	}

}