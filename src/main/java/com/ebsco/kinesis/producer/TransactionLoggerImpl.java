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


    protected static final BlockingQueue<TransactionLogging> txnLoggingQueue = new ArrayBlockingQueue<>(1);

    static {
        final ExecutorService exec = Executors.newCachedThreadPool();
        final KinesisProducer kinesisProducerThread = new KinesisProducer(txnLoggingQueue);
        exec.execute(kinesisProducerThread);

    }

    @Override public void log(TransactionLogging transactionLogging){
        LOG.info("txnLoggingQueue.remainingCapacity() ---> "+txnLoggingQueue.remainingCapacity());
        if(!txnLoggingQueue.offer(transactionLogging)){
            LOG.info("System will attempt to log "+transactionLogging.toString()+" in 5 seconds");
            try {
                Thread.sleep(5000);
                log(transactionLogging);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
