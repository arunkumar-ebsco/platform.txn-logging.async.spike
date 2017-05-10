package com.ebsco.kinesis.producer;

import com.ebsco.kinesis.dto.TransactionLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by aganapathy on 5/7/17.
 */
public class TransactionLoggerImpl implements TransactionLogger {

    final static Logger LOG = LoggerFactory.getLogger(TransactionLoggerImpl.class);


    protected static final BlockingQueue<TransactionLogging> txnLoggingQueue = new ArrayBlockingQueue<>(25);;

    static {
        final ExecutorService exec = Executors.newCachedThreadPool();
        final KinesisProducer kinesisProducerThread = new KinesisProducer(txnLoggingQueue);
        exec.execute(kinesisProducerThread);

    }

    @Override public void log(TransactionLogging transactionLogging) {
        if(!txnLoggingQueue.offer(transactionLogging)){
            LOG.info(transactionLogging.toString()+" is not sent to kinesis...");
        }
    }

    protected static Boolean validate(TransactionLogging transactionLogging) throws Exception{
        if(null != transactionLogging && null != transactionLogging.getSessionId() && null != transactionLogging.getPayload()){
            return Boolean.TRUE;
        } else {
            LOG.error(transactionLogging.toString()+" is not validated successfully ...");
            return Boolean.FALSE;
        }
    }

}
