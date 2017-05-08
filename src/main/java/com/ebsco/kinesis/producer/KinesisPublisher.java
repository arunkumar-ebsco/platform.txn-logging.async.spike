package com.ebsco.kinesis.producer;

import com.ebsco.kinesis.dto.TransactionLogging;

/**
 * Created by aganapathy on 5/7/17.
 */
public interface KinesisPublisher {

    void sendToKinesis(TransactionLogging transactionLogging);

}
