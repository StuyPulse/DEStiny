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
		return new SerialPort((SerialPortList.getPortNames())[0]);//new SerialPort("COM1");
	}

	public byte[] readData(int numberOfBytesToRead) {
		byte[] bytes;
		try {
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
}