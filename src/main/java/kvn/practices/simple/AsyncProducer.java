package kvn.practices.simple;

import kvn.practices.Constants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class AsyncProducer {

    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer(Constants.ASYNC_PRODUCER_GROUP);

        try {
            producer.start();
            producer.setRetryTimesWhenSendAsyncFailed(0);


            for (int i = 0; i < 100; i++) {

                Message msg = new Message(
                        "AsyncTopic1",
                        "AsyncTag1",
                        "order",
                        ("Hello Rocket " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
                );

                final int index = i;

                producer.send(msg, new SendCallback() {
                    public void onSuccess(SendResult sendResult) {
                        System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                    }

                    public void onException(Throwable throwable) {
                        System.out.printf("%-10d Exception %s %n", index, throwable);
                        throwable.printStackTrace();
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.shutdown();
        }

    }

}
