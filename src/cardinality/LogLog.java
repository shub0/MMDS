package cardinality;

import java.util.Arrays;

import algorithm.MurmurHash;

//ref: http://algo.inria.fr/flajolet/Publications/DuFl03-LNCS.pdf
public class LogLog implements CardianlityEstimator {

    /**
     * Gamma function computed using Mathematica
     * AccountingForm[
     * N[With[{m = 2^Range[0, 31]},
     * m (Gamma[-1/m]*(1 - 2^(1/m))/Log[2])^-m], 14]]
     */
    protected static final double[] mAlpha = {
            0,
            0.44567926005415,
            1.2480639342271,
            2.8391255240079,
            6.0165231584809,
            12.369319965552,
            25.073991603111,
            50.482891762408,
            101.30047482584,
            202.93553338100,
            406.20559696699,
            812.74569744189,
            1625.8258850594,
            3251.9862536323,
            6504.3069874480,
            13008.948453415,
            26018.231384516,
            52036.797246302,
            104073.92896967,
            208148.19241629,
            416296.71930949,
            832593.77309585,
            1665187.8806686,
            3330376.0958140,
            6660752.5261049,
            13321505.386687,
            26643011.107850,
            53286022.550177,
            106572045.43483,
            213144091.20414,
            426288182.74275,
            852576365.81999
    };

    protected final int k;
    protected int m;
    protected double Ca;
    protected byte[] M;

    public LogLog(int k) {
        if (k >= (mAlpha.length - 1)) {
            throw new IllegalArgumentException(String.format("Max k (%d) exceeded: k=%d", mAlpha.length - 1, k));
        }

        this.k = k;
        this.m = 1 << k;
        this.Ca = mAlpha[k];
        this.M = new byte[m];
    }

    public LogLog(byte[] M) {
        this.M = M;
        this.m = M.length;
        this.k = Integer.numberOfTrailingZeros(m);
        assert (m == (1 << k)) : "Invalid array size: M.length must be a power of 2";
        this.Ca = mAlpha[k];

    }

    @Override
    public byte[] getBytes() {
        return M;
    }

    public int sizeof() {
        return m;
    }

    @Override
    public long cardinality() {
    	int Rsum = 0;
    	for (byte m: M) {
    		Rsum += m;
    	}
        double Ravg = Rsum / (double) m;
        return (long) (Ca * Math.pow(2, Ravg));
    }

    @Override
    public boolean offerHashed(long hashedLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerHashed(int hashedInt) {
        boolean modified = false;
        // map hashInt to [0, 2^k] or get the first k bits
        int j = hashedInt >>> (Integer.SIZE - k);
        //  Get the value of k+1 bits to the end
        byte r = (byte) (Integer.numberOfLeadingZeros((hashedInt << k) | (1 << (k - 1))) + 1);
        modified = (M[j] < r);
        M[j] = modified ? r : M[j];
        return modified;
    }

    @Override
    public boolean offer(Object o) {
        int x = MurmurHash.hash32(o.toString());
        return offerHashed(x);
    }

    /**
     * Computes the position of the first set bit of the last Integer.SIZE-k bits
     *
     * @return Integer.SIZE-k if the last k bits are all zero
     */
    protected static int rho(int x, int k) {
        return Integer.numberOfLeadingZeros((x << k) | (1 << (k - 1))) + 1;
    }

    /**
     * @return this if estimators is null or no arguments are passed
     * @throws LogLogMergeException if estimators are not mergeable (all estimators must be instances of LogLog of the same size)
     */
    @Override
    public CardianlityEstimator merge(CardianlityEstimator... estimators) throws LogLogMergeException {
        if (estimators == null) {
            return new LogLog(M);
        }

        byte[] mergedBytes = Arrays.copyOf(this.M, this.M.length);
        for (CardianlityEstimator estimator : estimators) {
            if (!(this.getClass().isInstance(estimator))) {
                throw new LogLogMergeException("Cannot merge estimators of different class");
            }
            if (estimator.sizeof() != this.sizeof()) {
                throw new LogLogMergeException("Cannot merge estimators of different sizes");
            }
            LogLog ll = (LogLog) estimator;
            for (int i = 0; i < mergedBytes.length; ++i) {
                mergedBytes[i] = (byte) Math.max(mergedBytes[i], ll.M[i]);
            }
        }

        return new LogLog(mergedBytes);
    }

    /**
     * Merges estimators to produce an estimator for their combined streams
     *
     * @param estimators
     * @return merged estimator or null if no estimators were provided
     * @throws LogLogMergeException if estimators are not mergeable (all estimators must be the same size)
     */
    public static LogLog mergeEstimators(LogLog... estimators) throws LogLogMergeException {
        if (estimators == null || estimators.length == 0) {
            return null;
        }
        return (LogLog) estimators[0].merge(Arrays.copyOfRange(estimators, 1, estimators.length));
    }


    @SuppressWarnings("serial")
    protected static class LogLogMergeException extends Exception {

        public LogLogMergeException(String message) {
            super(message);
        }
    }
}
