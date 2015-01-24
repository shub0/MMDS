package algorithm;

import java.util.*;

import simulator.InputStream;

class Bucket {
	private int timestamp;
	private int size;
	Bucket(int timestamp, int size) {
		this.timestamp = timestamp;
		this.size = size;
	}
	static boolean merge(Bucket i, Bucket j) {
		if (i.size == j.size) {
			i.timestamp = Math.min(i.timestamp, j.timestamp);
			i.size *= 2;
			return true;
		}
		return false;
	}
	int getSize() {
		return size;
	}
	int getTimestamp() {
		return timestamp;
	}
}
/*
 * DGIM space requirement is (logN)^2
 * Error bound ~ 1 / (maxBucketsWithSameSize)
 */
public class DGIM extends OnesCounter{
	private int bufferSize;
	private int currentTimestamp;
	private int maxBucketsWithSameSize;
	ArrayList<Bucket> buckets;
	/**
	 * 
	 * @param maxWindowSize				max query window size
	 * @param maxBucketsWithSameSize	max number of buckets with same size, used for control error bound (default as 2)
	 */
	public DGIM(int maxWindowSize, int maxBucketsWithSameSize) {
		this.bufferSize = maxWindowSize;
		this.currentTimestamp = 0;
		this.maxBucketsWithSameSize = maxBucketsWithSameSize;
		buckets = new ArrayList<Bucket>();	
	}
	public DGIM(int windowSize) {
		this.bufferSize = windowSize;
		this.currentTimestamp = 0;
		this.maxBucketsWithSameSize = 2;		// default value
		buckets = new ArrayList<Bucket>();
	}
	/**
	 * Adjust buckets chain to ensure no more than {@maxBucketsWithSameSize} buckets with same size
	 * @param bucket: new buckets
	 */
	private void adjustBuckets(Bucket bucket) {
		buckets.add(0, bucket);
		int startIndex = 0;
		int endIndex = 0;
		// We can optimize this code by replacing buckets by a linked list
		while (startIndex < buckets.size()) {
			endIndex = startIndex;
			while (endIndex < buckets.size() - 1 && buckets.get(endIndex + 1).getSize() == buckets.get(startIndex).getSize()) {
				endIndex ++;
			}
			int currentCount = endIndex - startIndex + 1;
			if (currentCount <= maxBucketsWithSameSize) {
				break;
			}
			startIndex = endIndex - 1;
			// merge the last two 
			Bucket.merge(buckets.get(startIndex), buckets.get(endIndex));
			buckets.remove(endIndex);
		}
	}
	/**
	 * push a new bits into window
	 */
	public void push(boolean inputBit) {
		currentTimestamp += 1;
		// Clean up last one if out of boundary
		if (buckets.size() > 0 && buckets.get(buckets.size() - 1).getTimestamp() <= currentTimestamp - bufferSize) {
			buckets.remove(buckets.size() - 1);
		}
		if (inputBit) {
			Bucket currentBucket = new Bucket(currentTimestamp, 1);
			adjustBuckets(currentBucket);
		}
	}

	public int query() {
		return query(bufferSize);
	}
	/**
	 * @param windowSize	size of interesting window
	 * @return				number of 1s in the last @{windowSize} bits
	 */
	public int query(int windowSize) {
		int count = 0;
		windowSize = Math.min(windowSize, bufferSize);
		for (Bucket bucket: buckets) {
			count += bucket.getSize();
			if (bucket.getTimestamp() < currentTimestamp - windowSize)
				break;
		}
		return count;
	}
}
