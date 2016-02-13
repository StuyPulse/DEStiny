package edu.stuy.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

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
	    ByteBuffer wrapped = ByteBuffer.wrap(bytes);
	    wrapped.order(ByteOrder.LITTLE_ENDIAN);
	    return wrapped.getDouble();
	}

	public static byte[] doublesToBytes(double[] doubles) {
		byte[] byteArray = new byte[doubles.length * 8];// 8 bytes per double
        for (int i = 0; i < doubles.length; i++) {
			byte[] convertedDouble = doubleToBytes(doubles[i]);
			for(int j = 0; j < convertedDouble.length; j++) {
				byteArray[(i * 8) + j] = convertedDouble[j];
			}
		}
        return byteArray;
	}

	public static double[] bytesToDoubles(byte[] bytes) {
		double[] doubleArray = new double[bytes.length / 8];
		for(int i = 0; i < doubleArray.length; i++) {
			doubleArray[i] = bytesToDouble(
					Arrays.copyOfRange(bytes, i * 8, (i + 1) * 8)
					);
		}
		return doubleArray;
	}

}