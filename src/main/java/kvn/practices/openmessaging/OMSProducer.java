package kvn.practices.openmessaging;

import io.openmessaging.*;
import kvn.practices.Constants;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;

public class OMSProducer {

    public static void main(String[] args) {
        final String url = "openmessaging:rocketmq://127.0.0.1:9876/namespace";

        final MessagingAccessPoint messagingAccessPoint = MessagingAccessPointFactory.getMessagingAccessPoint(url);

        final Producer producer = messagingAccessPoint.createProducer();

        // 启动OMS访问点
        messagingAccessPoint.startup();
        // 启动生产
        producer.startup();


        try {
//            syncSend(producer);
            asyncSend(producer);
//            onewaySend(producer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        producer.shutdown();
        messagingAccessPoint.shutdown();

    }

    /**
     * 发送同步消息
     *
     * @param producer
     * @throws UnsupportedEncodingException
     */
    private static void syncSend(Producer producer) throws UnsupportedEncodingException {
        Message message = producer.createBytesMessageToTopic(Constants.OMS_SYNC_TOPIC_TITLE, "Hello OpenMessaging".getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult sendResult = producer.send(message);

        System.out.printf("Send sync message OK, msgId: %s%n", sendResult.messageId());
    }

    /**
     * 发送异步消息
     *
     * @param producer
     * @throws UnsupportedEncodingException
     */
    private static void asyncSend(Producer producer) throws UnsupportedEncodingException {

        Message message = producer.createBytesMessageToTopic(Constants.OMS_ASYNC_TOPIC_TITLE, "Hello OpenMessaging".getBytes(RemotingHelper.DEFAULT_CHARSET));
        final Promise<SendResult> resultPromise = producer.sendAsync(message);
        resultPromise.addListener(new PromiseListener<SendResult>() {
            @Override
            public void operationCompleted(Promise<SendResult> promise) {
                System.out.printf("Send async message OK, msgId: %s%n", promise.get());
            }

            @Override
            public void operationFailed(Promise<SendResult> promise) {
                System.out.printf("Send async message Failed, error: %s%n", promise.get());
            }
        });
    }

    /**
     * 发送单向消息
     *
     * @param producer
     * @throws UnsupportedEncodingException
     */
    private static void onewaySend(Producer producer) throws UnsupportedEncodingException {
        Message message = producer.createBytesMessageToTopic(Constants.OMS_ONEWAY_TOPIC_TITLE, "Hello OpenMessaging".getBytes(RemotingHelper.DEFAULT_CHARSET));
        producer.sendOneway(message);
        System.out.printf("Send oneway message OK %n");
    }
}
