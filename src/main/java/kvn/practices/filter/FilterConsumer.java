package kvn.practices.filter;

import kvn.practices.Constants;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class FilterConsumer {

    public static void main(String[] args) {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Constants.FILTER_CONSUMER_GROUP);


        try {
            // 订阅主题, 设置 SQL 过滤
            consumer.subscribe(Constants.FILTER_TOPIC_TITLE,
                    MessageSelector.bySql("idx between 3 and 4")
            );

            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                                ConsumeConcurrentlyContext consumeConcurrentlyContext) {

                    System.out.printf("%s%n", list);

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });


            // 启动消费
            consumer.start();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
