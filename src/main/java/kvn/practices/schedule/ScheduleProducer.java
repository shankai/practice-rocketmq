package kvn.practices.schedule;

import kvn.practices.Constants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class ScheduleProducer {

    public static void main(String[] args) {

        DefaultMQProducer producer = new DefaultMQProducer(Constants.SCHEDULE_PRODUCER_GROUP);

        try {
            // 启动生产
            producer.start();

            for (int i = 0; i < 10; i++) {

                // 构造消息
                Message message = new Message(
                        Constants.SCHEDULE_TOPIC_TITLE,
                        ("Hello schedule " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
                );

                // 延迟
                // This message will be delivered to consumer 10 seconds later.
                //
                message.setDelayTimeLevel(3);


                // 发送消息
                producer.send(message);

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 终止生产
            producer.shutdown();
        }

    }

}
