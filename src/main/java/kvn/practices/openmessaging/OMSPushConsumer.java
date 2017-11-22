package kvn.practices.openmessaging;

import io.openmessaging.*;
import io.openmessaging.rocketmq.domain.NonStandardKeys;
import kvn.practices.Constants;

public class OMSPushConsumer {

    public static void main(String[] args) {

        final String url = "openmessaging:rocketmq://127.0.0.1:9876/namespace";

        final MessagingAccessPoint messagingAccessPoint = MessagingAccessPointFactory.getMessagingAccessPoint(url);

        final PushConsumer consumer = messagingAccessPoint.createPushConsumer(OMS.newKeyValue().put(NonStandardKeys.CONSUMER_GROUP, "OMS_CONSUMER"));

        messagingAccessPoint.startup();
        System.out.printf("MessagingAccessPoint startup OK %n");

        consumer.startup();
        System.out.printf("PushConsumer startup OK %n");

        // 添加运行时终止钩子
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                // 终止接收
                consumer.shutdown();
                System.out.printf("PushConsumer shutdown OK %n");

                messagingAccessPoint.shutdown();
                System.out.printf("MessagingAccessPoint shutdown OK %n");
            }
        }));

        consumer.attachQueue(Constants.OMS_ASYNC_TOPIC_TITLE, new MessageListener() {
            @Override
            public void onMessage(Message message, ReceivedMessageContext receivedMessageContext) {
                System.out.printf("Receive one message: %s%n", message.headers().getString(MessageHeader.MESSAGE_ID));
                receivedMessageContext.ack();
            }
        });


    }
}
