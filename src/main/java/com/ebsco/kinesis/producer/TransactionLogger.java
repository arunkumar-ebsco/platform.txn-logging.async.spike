package com.ebsco.kinesis.producer;

import com.ebsco.kinesis.dto.TransactionLogging;

/**
 * Created by aganapathy on 5/7/17.
 */
public interface TransactionLogger {

    void log(TransactionLogging transactionLogging);

}
