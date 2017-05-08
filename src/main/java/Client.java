import com.ebsco.kinesis.java.library.KinesisPublisher;
import com.ebsco.kinesis.java.library.KinesisPublisherImpl;
import com.ebsco.kinesis.java.library.TransactionLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aganapathy on 5/1/17.
 * This is client stub code to test whether the messages are sent to Kinesis
 */
public class Client {

    final static Logger LOG = LoggerFactory.getLogger(KinesisPublisherImpl.class);


    public static void main(String[] args) {
        KinesisPublisher kinesisPublisher = new KinesisPublisherImpl();
        for (int i=0;i<=2;i++) {
            kinesisPublisher.sendToKinesis(new TransactionLogging(String.valueOf(i), "txnLogging"));
        }
        LOG.info("Client task done ...");
        }





}
