package simulator;
import algorithm.RingBuffer;
import algorithm.DGIM;

/**
 * Main class of stream sampler simulator
 * The purpose of this project is to compare the error bound and complexity of popular stream sample techniques
 * To simplify the testing scenario, all algorithms here are to implement a stream data sampler
 * Given a window size N, return the number of 1s in the last N bits. 
 * 
 * @author shubo
 *
 */
public class SimulatorController {
	static final double[] probabilities = {0.1, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5};
	static final int[]    windowSizes   = {10, 25, 50, 100, 250, 500};
	static StringBuilder   errorLog = new StringBuilder();
	public static double evaluate(int estimate, int truth) {
		return (truth == 0) ? 0 : (100 * (double) (truth - estimate) / (double) truth);
	}
	public static void runTest(double probability, int windowSize) {
		InputStream input = new InputStream(probability);
		RingBuffer  buffer = new RingBuffer(windowSize);
		DGIM		dgim   = new DGIM(windowSize, 10);
		double maxError = Double.MIN_VALUE;
		for (int index = 0; index < 100000; index ++) {
			boolean bit = input.output();
			buffer.push(bit);
			dgim.push(bit);
			int truth = buffer.query();
			int dgimEstimation = dgim.query();
			double error = Math.abs(evaluate(dgimEstimation, truth));
			if (error > 10.01) {
				for (boolean bitEx: buffer.getData()) {
					errorLog.append(bitEx ? 1 : 0);
				}
				errorLog.append("\nEst: " + dgimEstimation + ",Truth: " + truth + "\n");	
			}
			maxError = Math.max(error, maxError);
		}
		
		System.out.printf("Probability: %.2f, WindowSize: %d, DGIM max error: %.2f%%\n", probability, windowSize, maxError);
	}
	public static void main(String[] args) {
		for (int i = 0; i < probabilities.length; i++) {
			for (int j = 0; j < windowSizes.length; j ++) {
				runTest(probabilities[i], windowSizes[j]);
			}
		}
		System.out.println(errorLog.toString());
//		runTest(0.5, 25);
	}
	
}
