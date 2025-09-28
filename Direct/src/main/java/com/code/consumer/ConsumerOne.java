package com.code.consumer;

import com.code.util.Util;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;


public class ConsumerOne {
    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = Util.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "start");
        channel.queueBind(queueName, EXCHANGE_NAME, "end");
        System.out.println("消费者1等待接受消息.....");
        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("消费者1收到消息：" + message);
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费者1取消消费");
        };
        channel.basicConsume(queueName, callback, cancelCallback);
    }
}
