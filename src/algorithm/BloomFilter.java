package algorithm;



public class BloomFilter {
	BitMap data;
	public  BloomFilter(int numberBitMaps, int sizeBitMap) {
		data = new BitMap(sizeBitMap);
	}
	public boolean push(String value) {
		int hash = value.hashCode();
		if (!data.check(hash)) {
			data.set(hash);
			return false;
		}
		return true;
	}	
}
