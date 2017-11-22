package kvn.practices.schedule;

import kvn.practices.Constants;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class ScheduleConsumer {

    public static void main(String[] args) {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Constants.SCHEDULE_CONSUMER_GROUP);

        try {
            // 订阅主题, 分组
            consumer.subscribe(Constants.SCHEDULE_TOPIC_TITLE, "*");

            // 注册消息监听
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                                ConsumeConcurrentlyContext consumeConcurrentlyContext) {

                    for (MessageExt m : list) {

                        System.out.printf("Receive message "
                                + m.getMsgId()
                                + " at "
                                + (System.currentTimeMillis() - m.getStoreTimestamp())
                                + "ms later. %n");

                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

            // 启动消费
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }


    }

}
