package kvn.practices.broadcast;

import kvn.practices.Constants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class BroadcastProducer {

    public static void main(String[] args) {

        DefaultMQProducer producer = new DefaultMQProducer(Constants.BROADCAST_PRODUCER_GROUP);

        try {

            // 启动生产
            producer.start();

            for (int i = 0; i < 100; i++) {

                // 构造消息
                Message msg = new Message(
                        Constants.BROADCAST_TOPIC_TITLE,
                        "BroadcastTag1",
                        "Key" + i,
                        "Hello Broadcast".getBytes(RemotingHelper.DEFAULT_CHARSET)
                );

                // 发送消息
                SendResult sendResult = producer.send(msg);
                System.out.printf("%s%n", sendResult);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 终止生产
            producer.shutdown();
        }

    }
}
