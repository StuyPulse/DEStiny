package edu.stuy.util;
/**LogData.java
 * Basic class that stores data for Drive logging
 * Stores 4 Button values: Drive Left, Drive Right, Gearshift, Operator Right
 */
public class LogData {
	public double driveLeft;
	public double driveRight;
	public double operatorRightY;
	public boolean gearShift;
	
	public LogData(
			double driveLeft, 
			double driveRight, 
			double operatorRightY,
			boolean gearShift) { 
		this.driveLeft = driveLeft;
		this.driveRight = driveRight;
		this.gearShift = gearShift;
		this.operatorRightY = operatorRightY;
	}
	
	/* STANDARD FORMAT:
	 * driveLeft,driveRight,gearShift,operatorRight
	*/
	public String format() {
		return 
				String.valueOf(driveLeft) + "," +
				String.valueOf(driveRight) + "," +
				String.valueOf(operatorRightY) + "," +
				String.valueOf(gearShift);
	
	}
	
	public static LogData fromString(String string) throws Exception {	
		String[] stringArray = string.split(",");
		if (stringArray.length != 4) {
			throw new Exception("STRING READ " + stringArray.length + 
					" VALUES IN LOG FILE LINE, BUT SHOULD HAVE READ 4.");
		}
		double driveLeft = Double.valueOf(stringArray[0]);
		double driveRight = Double.valueOf(stringArray[1]);
		double operatorRightY = Double.valueOf(stringArray[3]);
		boolean gearShift = Boolean.valueOf(stringArray[3]);
		return new LogData(driveLeft,driveRight,operatorRightY, gearShift);
	}
}
