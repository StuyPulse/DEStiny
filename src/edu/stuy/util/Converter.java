package edu.stuy.util;

public class Converter {

	public static byte[] doubleToBytes(double number) {
		byte[] bytes = new byte[8];// 8, because doubles have 64 bits or 8 bytes
		long longNum = Double.doubleToLongBits(number);
		for(int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte)((longNum >> (8 * i)) & 0xff);
		}
		return bytes;
	}

	public static double bytesToDouble(byte[] bytes) {
		long longBits = 0;
		if (bytes.length != 8) {
			System.err.println("Cannot convert byte array to double! Length of array should be 8, "
					+ "but is " + bytes.length);
		} else {
			for(int i = 0; i < bytes.length; i++) {
				longBits = longBits | ((bytes[i] & 0xFFL) << (8 * i));//0b11111111
			}
		}
		return Double.longBitsToDouble(longBits);
	}
}
