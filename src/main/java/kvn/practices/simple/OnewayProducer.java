package kvn.practices.simple;

import kvn.practices.Constants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class OnewayProducer {

    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer(Constants.ONEWAY_PRODUCER_GROUP);

        try {
            producer.start();

            for (int i = 0; i < 100; i++) {

                Message msg = new Message(
                        "OnewayTopic1",
                        "OnewayTag1",
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
                );
                producer.sendOneway(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.shutdown();
        }

    }

}
