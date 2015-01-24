package simulator;
import java.util.Random;

public class InputStream {
	private static final int scaleFactor = 100;
	private double probability;
	private static Random randomGenerator = new Random();
	/**
	 * 
	 * @param probability: probability of true(1) in the stream
	 */
	public InputStream(double probability) {
		this.probability = probability;
	}
	/**
	 * 
	 * @return a output bit
	 */
	public boolean output() {
		int randomInt = randomGenerator.nextInt(scaleFactor);
		return randomInt < probability * scaleFactor;
	}
}
