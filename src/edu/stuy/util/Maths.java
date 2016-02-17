package edu.stuy.util;

public class Maths {
	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(sqr(x1 - x2) + sqr(y1 - y2));
	}
	
	public static double sqr(double n) {
		return n*n;
	}
}
