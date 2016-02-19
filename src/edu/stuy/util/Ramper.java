package edu.stuy.util;

import edu.stuy.util.shapes.Circle;
import edu.stuy.util.shapes.Function;
import edu.stuy.util.shapes.Line;
import edu.stuy.util.shapes.Sinusoid;

public class Ramper {
	public final int graphDivisions = 24;
	public double[] graph;

	private double currentSlope;

	// Constants
	private final double closeEnoughCutoff = 0.0001;
	private final int rampLength = 8; // How many iterations does it take to ramp (even number)
	/// SHOULD BE EVEN OR THERE MIGHT BE PROBLEMS (test that actually!)
	private int circleTimer;// If > 0, don't make another circle.

	public Ramper() {
		graph = new double[graphDivisions];
		currentSlope = 0;// Optional
		circleTimer = 0;
		// Sets graph to be empty and 0.
		graph[graph.length - 1] = 0;
		resetGraph();
	}

	private void graphFunction(int startx, double starty, int endx, Function function) {
		double diff = endx - startx;
		for(int i = startx; i < endx; i++) {
			graph[i] = function.calculate((i - startx) / diff);// + starty;
		}
	}

	private void increaseTargetViaMagnitude(int startx, int finalx, double target) {
		/* EXPLANATION:
		 * Say we have the target thats going up.
		 * 
		 * Now say we change that target to move up even more.
		 * 
		 * We can't "circle" our way out of it, because we're still moving up.
		 * 
		 * So instead we increase each step, but do so linearly so that it's smooth.
		 */
		double slope = (target - graph[finalx - 1]) / ((double)(finalx - 1) - (double)startx);
		if (Math.signum(slope) == Math.signum(finalx - startx)) {
			int distance = finalx - startx;
			for(int i = 0; i < distance; i++) {
				// !!!!!!!!!!!!!!!!!! MIGHT NEED TO CHANGE TO i <= distance
				graph[i] += i * slope;
			}
		}
	}

	private void resetGraph() {
		double resetValue = graph[graph.length - 1];// Last value
		for(int i = 0; i < graph.length; i++) {
			graph[i] = resetValue;
		}
	}

	private boolean closeEnough(double v1, double v2) {
		return Math.abs(v2 - v1) < closeEnoughCutoff;
	}

	private double getCurrentSlope() {
		return graph[1] - graph[0];
	}

	public double getValue() {
		return graph[0];
	}

	public void update() {
		circleTimer --;
		if (circleTimer < 0) {
			circleTimer = 0;
		}
		currentSlope = getCurrentSlope();
		for(int i = 1; i < graph.length; i++) {
			graph[i - 1] = graph[i];
		}
		graph[graph.length - 1] = graph[graph.length - 2];
		// Makes end of graph flat.
		System.out.println(getValue());
	}

	public void setTarget(double target) {
		if (closeEnough(currentSlope, 0)) {//(closeEnough(currentSlope, 0)) {
			System.out.println("CLOSE ENOUGH");
			// Just creates line on top of current one without smoothing
			//resetGraph();
			if (!closeEnough(getValue(), target)) {
				System.out.println("NEW GRAPH (" + getValue() + " ~= " + target);
				graphFunction(0, getValue(), graph.length, 
						new Sinusoid(getValue(), target));
			}
		} else if (!closeEnough(target, getValue())) {
			/// IMPORTANT NOTE:
			/*
			 * When dealing with functions, we have to convert the graph x
			 * into a value between 0 and 1. 
			 * 		converted = graph[whatever] / graph.length
			 */

			// SECOND SINEWAVE
			boolean doCircle = ( ((currentSlope > 0) && (target < getValue()))
					|| ((currentSlope < 0) && (target > getValue())) );

			if ((doCircle) && (circleTimer == 0)) {
				circleTimer = rampLength;
				graphFunction(rampLength/2,getValue(), graph.length,
						new Sinusoid(getValue(), target));
				double slope2 = graph[rampLength + 1] - graph[rampLength];
				
				Line line1 = new Line(currentSlope, getValue());
				Line line2 = new Line(slope2, graph[rampLength] / graph.length);
				//TODO: FIX LINE2 WITH SECOND CONSTRUCTOR
				
				Circle interCircle = null;
				if (currentSlope > 0) {
					System.out.println("Circle Up");
					// slope2: Slope of next sine wave.
					interCircle = new Circle(line1,line2,0.0,
							(double)rampLength / graph.length,true);
					graphFunction(0,getValue(), rampLength, interCircle);
				} else if (currentSlope < 0) {
					System.out.println("Circle Down");
					// slope2: Slope of next sine wave.
					interCircle = new Circle(line1,line2,0.0,
							(double)rampLength / graph.length,false);
					graphFunction(0,getValue(), rampLength, interCircle);
				}
			} else {
				/* 
				 * Now we assume that we will be moving in the same direction, and 
				 * that the sign of the difference btwn
				 * the target and the current value remains the same.
				 */
				increaseTargetViaMagnitude(0, graph.length, target);
			}
		}
	}
	/* FOR TESTING
	public static void main(String args[]) {
		Ramper ramper = new Ramper();
		RamperTimeLoop timeLoop = new RamperTimeLoop();
		ramper.setTarget(0);
		timeLoop.addRamper(ramper);
		timeLoop.start();
	}
	*/
}