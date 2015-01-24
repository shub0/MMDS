package algorithm;


public class BitMap {
	boolean[] data;
	int size;
	public BitMap(int size) {
		this.size = size;
		data = new boolean[size];
	}
	public void set(int index) {
		data[index % size] = true;
	}
	public boolean check(int index) {
		return data[index % size];
	}
}