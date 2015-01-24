package algorithm;

abstract class OnesCounter {
	private int windowSize;
	abstract public void push(boolean inputBit);
	abstract public int query();
}
