package kvn.practices.filter;

import kvn.practices.Constants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class FilterProducer {

    public static void main(String[] args) {

        DefaultMQProducer producer = new DefaultMQProducer(Constants.FILTER_PRODUCER_GROUP);

        try {
            // 启动生产
            producer.start();

            for (int i = 0; i < 100; i++) {

                // 构造消息
                Message message = new Message(
                        Constants.FILTER_TOPIC_TITLE,
                        "FilterTag1",
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
                );

                // 设置属性值
                message.putUserProperty("idx", String.valueOf(i));

                // 发送消息
                SendResult sendResult = producer.send(message);

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
