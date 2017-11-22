package kvn.practices.openmessaging;

import io.openmessaging.*;
import io.openmessaging.rocketmq.domain.NonStandardKeys;
import kvn.practices.Constants;

public class OMSPullConsumer {
    public static void main(String[] args) {
        final String url = "openmessaging:rocketmq://127.0.0.1:9876/namespace";
        final MessagingAccessPoint messagingAccessPoint = MessagingAccessPointFactory.getMessagingAccessPoint(url);

        final PullConsumer consumer = messagingAccessPoint.createPullConsumer(
                Constants.OMS_ASYNC_TOPIC_TITLE,
                OMS.newKeyValue().put(NonStandardKeys.CONSUMER_GROUP, "OMS_CONSUMER"));

        messagingAccessPoint.startup();
        System.out.printf("MessagingAccessPoint startup OK %n");

        consumer.startup();
        System.out.printf("Consumer startup OK %n");

        Message message = consumer.poll();

        if (message != null) {
            String msgId = message.headers().getString(MessageHeader.MESSAGE_ID);
            System.out.printf("Receive one message: %s%n", msgId);
            consumer.ack(msgId);
        }

        consumer.shutdown();
        messagingAccessPoint.shutdown();

    }
}