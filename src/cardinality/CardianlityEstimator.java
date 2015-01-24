package cardinality;

import java.io.IOException;


public interface CardianlityEstimator {

    /**
     * @param o stream element
     * @return false if the value returned by cardinality() is unaffected by the appearance of o in the stream.
     */
    boolean offer(Object o);

    /**
     * Offer the value as a hashed long value
     *
     * @param hashedLong - the hash of the item to offer to the estimator
     * @return false if the value returned by cardinality() is unaffected by the appearance of hashedLong in the stream
     */
    boolean offerHashed(long hashedLong);

    /**
     * Offer the value as a hashed long value
     *
     * @param hashedInt - the hash of the item to offer to the estimator
     * @return false if the value returned by cardinality() is unaffected by the appearance of hashedInt in the stream
     */
    boolean offerHashed(int hashedInt);

    /**
     * @return the number of unique elements in the stream or an estimate thereof
     */
    long cardinality();

    /**
     * @return size in bytes needed for serialization
     */
    int sizeof();

    /**
     * @return
     * @throws IOException
     */
    byte[] getBytes() throws IOException;

    /**
     * Merges estimators to produce a new estimator for the combined streams
     * of this estimator and those passed as arguments.
     * <p/>
     * Nor this estimator nor the one passed as parameters are modified.
     *
     * @param estimators Zero or more compatible estimators
     * @throws CardinalityMergeException If at least one of the estimators is not compatible with this one
     */
    CardianlityEstimator merge(CardianlityEstimator... estimators) throws Exception;
}
