package com.zcbspay.platform.hz.realtime.common.utils.secret;

public final class PrintUtils {
	private final static char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private PrintUtils() {
	}

	public static byte[] toBytes(int a) {
		return new byte[] { (byte) (0x000000ff & (a >>> 24)), (byte) (0x000000ff & (a >>> 16)),
				(byte) (0x000000ff & (a >>> 8)), (byte) (0x000000ff & (a)) };
	}

	public static int toInt(byte[] b, int s, int n) {
		int ret = 0;

		final int e = s + n;
		for (int i = s; i < e; ++i) {
			ret <<= 8;
			ret |= b[i] & 0xFF;
		}
		return ret;
	}

	public static int toIntR(byte[] b, int s, int n) {
		int ret = 0;

		for (int i = s; (i >= 0 && n > 0); --i, --n) {
			ret <<= 8;
			ret |= b[i] & 0xFF;
		}
		return ret;
	}

	public static int toInt(byte... b) {
		int ret = 0;
		for (final byte a : b) {
			ret <<= 8;
			ret |= a & 0xFF;
		}
		return ret;
	}

	public static String toHexStringII(byte[] d, int s, int n) {
		final char[] ret = new char[n * 3];
		final int e = s + n;

		int x = 0;
		for (int i = s; i < e; ++i) {
			final byte v = d[i];
			ret[x++] = HEX[0x0F & (v >> 4)];
			ret[x++] = HEX[0x0F & v];
			ret[x++] = ' ';
		}
		return new String(ret);
	}
	public static String toHexString(byte[] d, int s, int n) {
		final char[] ret = new char[n * 2];
		final int e = s + n;

		int x = 0;
		for (int i = s; i < e; ++i) {
			final byte v = d[i];
			ret[x++] = HEX[0x0F & (v >> 4)];
			ret[x++] = HEX[0x0F & v];
		//	ret[x++] = ' ';
		}
		return new String(ret);
	}

	public static String intToHexString(int val) {
		final char[] ret = new char[4];

		int x = 0;
		final int v1 = (val & 0xFF00) >> 8;
		final int v2 = (val & 0x00FF);

		if (v1 != 0) {
			ret[x++] = HEX[0x0F & (v1 >> 4)];
			ret[x++] = HEX[0x0F & v1];
		}

		ret[x++] = HEX[0x0F & (v2 >> 4)];
		ret[x++] = HEX[0x0F & v2];

		return new String(ret, 0, v1 != 0 ? 4 : 2);
	}

	public static String toHexStringR(byte[] d, int s, int n) {
		final char[] ret = new char[n * 2];

		int x = 0;
		for (int i = s + n - 1; i >= s; --i) {
			final byte v = d[i];
			ret[x++] = HEX[0x0F & (v >> 4)];
			ret[x++] = HEX[0x0F & v];
		}
		return new String(ret);
	}

	public static int parseInt(String txt, int radix, int def) {
		int ret;
		try {
			ret = Integer.valueOf(txt, radix);
		} catch (Exception e) {
			ret = def;
		}

		return ret;
	}

	public static void printHex(String txt) {
		printHex(txt.getBytes());
	}

	public static void printHex(byte[] d) {
		System.out.println(toHexString(d, 0, d.length));
	}

	/**
	 * 以hex进制显示报文（一般用于不可读字符）
	 * 
	 * @param b
	 * @param beginIndex
	 * @param length
	 * @return
	 */
	public static String printMsgHex(byte[] b, int beginIndex, int length) {

		String info = new String();

		int line = 1;
		// info += "================================\n";
		info += "===================" + length + "=============\n";
		for (int pos = 0; pos < length; line++) {
			int linesize = length - pos < 16 ? (length - pos) : 16;

			info += String.format("[%03d] ", line);
			// info += String.format("%49.49s", toHexString(b, pos, linesize));
			info += toHexString(b, beginIndex, linesize);

			// 不足16字节
			for (int n = linesize; n < 16; n++) {
				// if (n == linesize)
				// {
				// info += "-- ";
				// }
				info += "-- ";

			}

			info += " : ";

			for (int i = 0; i < linesize; i++) {
				// if (b[pos + i] < 0 || b[pos + i] == '\n' || b[pos + i] ==
				// '\t') {
				// info += ".";
				// continue;
				// }
				if (b[beginIndex + i] < 0x20 || b[beginIndex + i] > 0xFF) {
					info += ".";
					continue;
				}
				info += String.format("%c", b[beginIndex + i]);
			}

			pos += linesize;
			beginIndex+=linesize;
			info += "\n";

		}
		info += "================================\n";
		return info;
	}
	
	public static void disPlay(String str)
	{
		System.out.println("disPlay:"+str);
	}

}
