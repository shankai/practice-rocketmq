package kvn.practices.order;

import kvn.practices.Constants;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class OrderedConsumer {

    public static void main(String[] args) {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Constants.ORDERED_CONSUMER_GROUP);

        try {
            // 设置消息消费顺序
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

            // 订阅主题, 分组
            consumer.subscribe(Constants.ORDERED_TOPIC_TITLE, "OrderedTag1 || OrderedTag3");

            // 注册消息监听
            consumer.registerMessageListener(new MessageListenerOrderly() {

                AtomicLong consumeTimes = new AtomicLong(0);

                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list,
                                                           ConsumeOrderlyContext consumeOrderlyContext) {

                    consumeOrderlyContext.setAutoCommit(false);

                    System.out.printf(Thread.currentThread().getName() + "Receive New Messages: " + list + "%n");

                    this.consumeTimes.incrementAndGet();

                    if (this.consumeTimes.get() % 2 == 0) {
                        return ConsumeOrderlyStatus.SUCCESS;
                    } else if (this.consumeTimes.get() % 3 == 0) {
                        return ConsumeOrderlyStatus.ROLLBACK;
                    } else if (this.consumeTimes.get() % 4 == 0) {
                        return ConsumeOrderlyStatus.COMMIT;
                    } else if (this.consumeTimes.get() % 5 == 0) {
                        consumeOrderlyContext.setSuspendCurrentQueueTimeMillis(3000);
                        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                    }

                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });

            // 启动消费
            consumer.start();

            System.out.printf("Consumer Started. %n");

        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }

}
