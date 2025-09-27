package code.consumer;

import code.util.Util;
import com.rabbitmq.client.*;


public class ConsumerOne {
    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = Util.getChannel();
        System.out.println("消费者1等待接受消息.....");
        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("消费者1收到消息：" + message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            System.out.println("消费者1已手动应答消息");
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费者1取消消费");
        };
        channel.basicConsume(QUEUE_NAME, false, callback, cancelCallback);
    }
}
