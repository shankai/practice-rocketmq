package kvn.practices.broadcast;

import kvn.practices.Constants;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

public class BroadcastConsumer {

    public static void main(String[] args) {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Constants.BROADCAST_CONSUMER_GROUP);

        // 设置消息获取顺序
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 设置消息模式为广播
        consumer.setMessageModel(MessageModel.BROADCASTING);

        try {

            // 订阅主题,分组
            consumer.subscribe(Constants.BROADCAST_TOPIC_TITLE, "BroadcastTag1");

            // 设置消息监听
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                                ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    System.out.printf(Thread.currentThread().getName() + "Receive New Messages: " + list + "%n");

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

            // 启动消费
            consumer.start();

            System.out.printf("Broadcast Consumer Started. %n");


        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }
}
