package nz.org.geonet;

//~--- non-JDK imports --------------------------------------------------------

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;

import org.junit.BeforeClass;
import org.junit.Test;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collection;

/**
 * Unit test for simple Sqs.
 */
public class SqsTest {
    static AmazonSQS sqs;

    @BeforeClass
    public static void setUp() throws Exception {
        sqs = new AmazonSQSClient(
            new PropertiesCredentials(SqsTest.class.getResourceAsStream("AwsCredentials.properties")));
        sqs.setEndpoint("https://ap-southeast-1.queue.amazonaws.com/");
    }

    @Test
    public void testSend() {
        SendMessageBatchRequest sendMessageBatchRequest = new SendMessageBatchRequest();

        sendMessageBatchRequest.setQueueUrl("quake-felt-test");

        Collection<SendMessageBatchRequestEntry> messages;

        for (int j = 0; j < 100; j++) {
            messages = new ArrayList<SendMessageBatchRequestEntry>();

            for (int i = 0; i < 10; i++) {
                SendMessageBatchRequestEntry sendMessageBatchRequestEntry = new SendMessageBatchRequestEntry();

                sendMessageBatchRequestEntry.setId(String.valueOf(i));
                sendMessageBatchRequestEntry.setMessageBody("<point><latitude>" + (-45 + (Math.random() * 10))
                        + "</latitude><longitude>" + (170 + (Math.random() * 10)) + "</longitude></point>");
                messages.add(sendMessageBatchRequestEntry);
            }

            sendMessageBatchRequest.setEntries(messages);
            sqs.sendMessageBatch(sendMessageBatchRequest);
        }
    }
}
