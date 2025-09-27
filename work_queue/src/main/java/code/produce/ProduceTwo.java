package code.produce;

import code.util.Util;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;


public class ProduceTwo {
    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = Util.getChannel();
        System.out.println("消费者2等待接受消息.....");
        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("消费者2收到消息：" + message);
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费者2取消消费");
        };
        channel.basicConsume(QUEUE_NAME, true, callback, cancelCallback);
    }
}
