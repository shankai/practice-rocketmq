package kvn.practices.order;

import kvn.practices.Constants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

public class OrderedProducer {

    public static void main(String[] args) {

        MQProducer producer = new DefaultMQProducer(Constants.ORDERED_PRODUCER_GROUP);

        try {
            // 启动生产
            producer.start();

            // 标签分组
            String[] tags = new String[]{"OrderedTag1", "OrderedTag2", "OrderedTag3", "OrderedTag4", "OrderedTag5"};

            for (int i = 0; i < 10; i++) {

                // 构造消息
                Message msg = new Message(
                        Constants.ORDERED_TOPIC_TITLE,
                        tags[i % tags.length],
                        "Content" + i,
                        ("Hello Ordered-Producer" + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
                );

                int orderId = i % 10;

                // 发送消息
                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {

                            @Override
                            public MessageQueue select(List<MessageQueue> list, Message message, Object oId) {
                                Integer id = (Integer) oId;
                                int index = id % list.size();
                                return list.get(index);
                            }
                        },
                        orderId);

                // 打印发送结果
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
