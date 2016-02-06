package edu.stuy.util;

import java.util.Arrays;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Receiver {
	SerialPort mainPort;

	public Receiver() {
		mainPort = initializePort();
	}

	public SerialPort initializePort() {
		try {
			return new SerialPort((SerialPortList.getPortNames())[0]);//new SerialPort("COM1");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("NO PORTS FOUND!");
			e.printStackTrace();
			return null;
		}
	}

	public byte[] readData(int numberOfBytesToRead) {
		byte[] bytes;
		try {
			if (mainPort.isOpened()) {
				System.out.println("PORT IS ALREADY OPENED!!!!!!!!");
			}
 			mainPort.openPort();
			mainPort.setParams(9600, 8, 1, 0);
			bytes = mainPort.readBytes(numberOfBytesToRead);
			mainPort.closePort();
			return bytes;
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		return null;
	}

	public double[] readDoubles(int numberOfDoubles) {
		double[] doubleArray = new double[numberOfDoubles];
		byte[] bytes = readData(numberOfDoubles * 8);
		for(int i = 0; i < numberOfDoubles; i++) {
			doubleArray[i] = Converter.bytesToDouble(
					Arrays.copyOfRange(bytes, i * 8, (i + 1) * 8)
					);
		}
		return doubleArray;
 	}

	public double[] readVector() {
		byte firstByte = readData(1)[0];
		if (firstByte == 255) {
			return readDoubles(1);
		}
		return new double[0];// Empty double
	}

	public static void main(String args[]) {
		Receiver r = new Receiver();
		double[] doubles = r.readVector();
		System.out.println("Printing Doubles... Fingers Crossed.");
		for(int i = 0; i < doubles.length; i++) {
			System.out.println(doubles[i]);
		}
		System.out.println("DONE");
	}
}