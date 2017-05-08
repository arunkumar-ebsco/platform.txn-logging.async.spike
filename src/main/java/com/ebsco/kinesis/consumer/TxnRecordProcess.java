package com.ebsco.kinesis.consumer;

import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.KinesisClientLibDependencyException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ThrottlingException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.lib.checkpoint.SentinelCheckpoint;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.clientlibrary.types.ExtendedSequenceNumber;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.model.Record;
import com.google.common.collect.Lists;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

public class TxnRecordProcess implements IRecordProcessor {

    final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TxnRecordProcess.class);


    private final static CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
    private static final long BACKOFF_TIME_IN_MILLIS = 3000;
    private static final int NUM_RETRIES = 5;
    protected static final List<String> readRecordList = Lists.newArrayList();

    @Override
    public void initialize(InitializationInput initializationInput) {
        initializationInput.withExtendedSequenceNumber(new ExtendedSequenceNumber(SentinelCheckpoint.TRIM_HORIZON
                .toString()));

    }

    @Override
    public void processRecords(ProcessRecordsInput processRecordsInput) {
        try {
            List<Record> records = processRecordsInput.getRecords();
            System.out.println(records);
            if (!records.isEmpty()) {
                records.forEach(record -> {
                    String data;
                    try {
                        data = decoder.decode(record.getData()).toString();
                        LOGGER.info(data);
                        readRecordList.add(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
                try {
                    Thread.sleep(BACKOFF_TIME_IN_MILLIS);
                    processRecordsInput.getCheckpointer().checkpoint();
                } catch (InvalidStateException | InterruptedException | ShutdownException e) {
                    e.printStackTrace();
                }

            }

        } catch (KinesisClientLibDependencyException | ThrottlingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown(ShutdownInput shutdownInput) {
        System.out.println("in the shutdown method...");
        if (shutdownInput.getShutdownReason().equals(ShutdownReason.TERMINATE)) {
            checkpoint(shutdownInput.getCheckpointer());
        }

    }

    private void checkpoint(IRecordProcessorCheckpointer checkpointer) {
        for (int i = 0; i < NUM_RETRIES; i++) {
            try {
                checkpointer.checkpoint();
                break;
            } catch (ShutdownException | InvalidStateException e) {
                LOGGER.info("skipping checkpoint.", e);
                break;
            } catch (ThrottlingException e) {
                if (i >= (NUM_RETRIES - 1)) {
                    LOGGER.info("Checkpoint failed after " + (i + 1) + "attempts.", e);
                    break;
                } else {
                    LOGGER.info("Transient issue when checkpointing - attempt " + (i + 1) + " of "
                            + NUM_RETRIES);
                }
            }
            try {
                Thread.sleep(BACKOFF_TIME_IN_MILLIS);
            } catch (InterruptedException e) {
                LOGGER.info("Interrupted sleep", e);
            }

        }
    }
}

