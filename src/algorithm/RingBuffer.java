package algorithm;

/**
 * This is the ground truth implemented by a ring buffer
 * The ring buffer implementation provides the exact count 
 * 
 * @author shubo
 *
 */
public class RingBuffer extends OnesCounter{
	private int windowSize;
	private boolean[] buffer;
	private int currentSize;
	/**
	 * 
	 * @param windowSize: window size of interests
	 */
	public RingBuffer(int windowSize) {
		if (windowSize <= 0) {
			throw new IllegalArgumentException("window size must be positive");
		}
		this.windowSize = windowSize;
		buffer = new boolean[windowSize];
		currentSize = 0;
	}
	/**
	 * 
	 * @param inputBit: input bit, true (1) or false (0)
	 */
	public void push(boolean inputBit) {
		currentSize ++;
		currentSize %= windowSize;
		buffer[currentSize] = inputBit;
	}
	/**
	 *
	 * @return number of 1s in the most recent windowSize bits
	 */
	public int query() {
		int currentBits = 0;
		for (boolean ex: buffer) {
			if (ex)
				currentBits ++;
		}
		return currentBits;
	}
	/**
	 * 
	 * @return space complexity
	 */
	public int getSize() {
		return windowSize;
	}
	
	public boolean[] getData() {
		return buffer;
	}
}
