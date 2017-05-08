package com.ebsco.kinesis.java.library;

/**
 * Created by aganapathy on 5/7/17.
 */
public interface KinesisPublisher {

    void sendToKinesis(TransactionLogging transactionLogging);

}
