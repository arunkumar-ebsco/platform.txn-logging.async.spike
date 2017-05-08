import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TxnReadDataKinesis {

    private static final String APPLICATION_NAME = "NewTable";
    private static final String STREAM_NAME = "kinesis_e2e_test";

    public static void main(String[] args) throws Exception {
        String workerId = InetAddress.getLocalHost().getCanonicalHostName() + ":" + UUID.randomUUID();
        KinesisClientLibConfiguration config = new KinesisClientLibConfiguration(APPLICATION_NAME, STREAM_NAME,
                new ProfileCredentialsProvider(), workerId);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date todate1 = cal.getTime();
        config.withInitialPositionInStream(InitialPositionInStream.TRIM_HORIZON);
        config.withCallProcessRecordsEvenForEmptyRecordList(false).withTimestampAtInitialPositionInStream(todate1);
        IRecordProcessorFactory recordProcessorFactory = new TxnRecordProcessorFactory();
        Worker worker = new Worker.Builder().recordProcessorFactory(recordProcessorFactory).config(config).build();
        worker.run();

    }
}
