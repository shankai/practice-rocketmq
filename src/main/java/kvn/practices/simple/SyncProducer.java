package kvn.practices.simple;

import kvn.practices.Constants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class SyncProducer {

    public static void main(String[] args) {

        DefaultMQProducer producer = new DefaultMQProducer(Constants.SYNC_PRODUCER_GROUP);

        try {
            producer.start();

            for (int i = 0; i < 100; i++) {

                // Topic, Tag, Body

                Message msg = new Message(
                        "SyncTopic1",
                        "SyncTag1",
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
                );

                SendResult sendResult = producer.send(msg);
                System.out.printf("%s%n", sendResult);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.shutdown();
        }


    }
}
