package algorithm;

import java.util.BitSet;


/*
 * Java implementation of PCSA 
 * Reference 
 * http://www.mathcs.emory.edu/~cheung/papers/StreamDB/Probab/1985-Flajolet-Probabilistic-counting.pdf
 * 
 * */
public class PCSA {
	private int numMaps;
	private BitSet[] bitMaps;
	private static final double PHI = 0.77351; // Magic constant
	private static final int SIZE = Integer.SIZE;
	public PCSA(int log2NumMaps) {
		numMaps = 2 ^ log2NumMaps;
		bitMaps = new BitSet[numMaps];
		for (int index = 0; index < numMaps; index++) {
			bitMaps[index] = new BitSet(SIZE);
		}
	}
	private static int mostSignificantBit(int number) {
		int mask = 1 << (Integer.SIZE - 1);
		for (int bitIndex = Integer.SIZE; bitIndex > 0; bitIndex --) {
			if ((number & mask) != 0) {
				return Integer.SIZE - bitIndex;
			}
			mask >>>= 1;
		}
		return Integer.SIZE;
	}
	public void push(String input) {
		int hashCode = MurmurHash.hash32(input);
		int bitMapIndex = hashCode() % numMaps;
		int index = mostSignificantBit(hashCode / numMaps);
		if (index < SIZE) bitMaps[bitMapIndex].set(index);
	}
	
	public int cardinality() {
		int totalCount = 0;
		for (int bitMapIndex = 0; bitMapIndex < numMaps; bitMapIndex ++) {
			int currentCount = 0;
			int index = 0;
			while(bitMaps[bitMapIndex].get(index) && index < SIZE) {
				currentCount ++;
				index ++;
			}
			totalCount += currentCount;
		}
		return (int)(numMaps / PHI * Math.pow(2, totalCount / numMaps));
	}
}
