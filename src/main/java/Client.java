import com.ebsco.kinesis.producer.TransactionLogger;
import com.ebsco.kinesis.producer.TransactionLoggerImpl;
import com.ebsco.kinesis.dto.TransactionLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aganapathy on 5/1/17.
 * This is client stub code that creates TxnLogging messages to kinesis.
 */
public class Client {

    final static Logger LOG = LoggerFactory.getLogger(TransactionLoggerImpl.class);


    public static void main(String[] args) {
        TransactionLogger transactionLogger = new TransactionLoggerImpl();
        for (int i=0;i<=2;i++) {
            transactionLogger.log(new TransactionLogging(String.valueOf(i), +i+" Sample Payload"));
        }
        LOG.info("Client task done ...");
        }





}
