package kvn.practices.batch;

import kvn.practices.Constants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.ArrayList;
import java.util.List;

public class BatchProducer {

    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer(Constants.BATCH_PRODUCER_GROUP);

        final String topic = "BatchTopic1";
        final String tag = "BatchTag1";

        try {
            producer.start();

            List<Message> msgs = new ArrayList<Message>();

            msgs.add(new Message(topic, tag, "BATCH-ORDER-1", "Hello Batch 1".getBytes(RemotingHelper.DEFAULT_CHARSET)));
            msgs.add(new Message(topic, tag, "BATCH-ORDER-2", "Hello Batch 2".getBytes(RemotingHelper.DEFAULT_CHARSET)));
            msgs.add(new Message(topic, tag, "BATCH-ORDER-3", "Hello Batch 3".getBytes(RemotingHelper.DEFAULT_CHARSET)));

            producer.send(msgs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.shutdown();
        }
    }
}
