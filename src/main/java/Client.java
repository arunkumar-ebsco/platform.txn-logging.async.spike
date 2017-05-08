import com.ebsco.kinesis.producer.KinesisPublisher;
import com.ebsco.kinesis.producer.KinesisPublisherImpl;
import com.ebsco.kinesis.dto.TransactionLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aganapathy on 5/1/17.
 * This is client stub code that creates TxnLogging messages to kinesis.
 */
public class Client {

    final static Logger LOG = LoggerFactory.getLogger(KinesisPublisherImpl.class);


    public static void main(String[] args) {
        KinesisPublisher kinesisPublisher = new KinesisPublisherImpl();
        for (int i=0;i<=2;i++) {
            kinesisPublisher.sendToKinesis(new TransactionLogging(String.valueOf(i), +i+" Sample Payload"));
        }
        LOG.info("Client task done ...");
        }





}
