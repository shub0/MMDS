package cardinality;

import java.util.Arrays;

public class SuperLogLog extends LogLog{
	
	double theta;
	public SuperLogLog(byte[] M, double theta) {
		super(M);
		this.theta = theta;
	}
    public SuperLogLog(byte[] M) {
    	this(M, 0.7);
    }
    public SuperLogLog(int k, double theta) {
    	super(k);
    	this.theta = theta;
    }
    public SuperLogLog(int k) {
    	this(k, 0.7);
    }
    @Override
    public long cardinality() {
    	int Rsum = 0;
    	int m0 = (int) (theta * m);
    	Arrays.sort(M);
    	for (int index = 0; index < m0; index ++) {
    		Rsum += M[index];
    	}
        double Ravg = Rsum / (double) m0;
        return (long) (Ca * Math.pow(2, Ravg));
    }
}
