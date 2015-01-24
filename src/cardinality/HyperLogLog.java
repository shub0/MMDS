package cardinality;

public class HyperLogLog extends LogLog{
	
	private static double getAlpha(int k) {
		switch (k) {
		case 4:
			return 0.673;
		case 5:
			return 0.697;
		case 6:
			return 0.709;
		default:
			return 0.7213/ (1 + 1.079 / (1 << k));
		}
	}
	public HyperLogLog(byte[] M) {
		super(M);
		this.Ca = getAlpha(k);
	}
	public HyperLogLog(int k) {
		super(k);
		this.Ca = getAlpha(k);
	}
	
	@Override
    public long cardinality() {
    	double Rsum = 0;
    	for (byte m: M) {
    		Rsum += 1 / (1 << m);
    	}
        double DVEstiamtion = Ca * Math.pow(m, 2) / Rsum;
        double DV = 0;
        if (DVEstiamtion < 5 / 2 * m) {
        	int V = 0;
        	for (byte b: M) {
        		if (b == 0) {
        			V ++;
        		}
        	}
        	if (V == 0)
        		DV = DVEstiamtion;
        	else 
        		DV = m * Math.log(m / V);
        } else if (DVEstiamtion < 1 << Integer.SIZE / 30) {
        	DV = DVEstiamtion;
        } else {
        	DV = - (1 << Integer.SIZE) * Math.log(1 - DVEstiamtion / (1 <<Integer.SIZE));
        }
        return Math.round(DV);
    }

}
