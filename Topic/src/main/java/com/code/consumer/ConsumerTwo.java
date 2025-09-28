package com.code.consumer;

import com.code.util.Util;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;


public class ConsumerTwo {
    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = Util.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.name");
        channel.queueBind(queueName, EXCHANGE_NAME, "speed.#");
        System.out.println("消费者2等待接受消息.....");
        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("消费者2收到消息：" + message+"，路由键：" + delivery.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费者2取消消费");
        };
        channel.basicConsume(queueName, callback, cancelCallback);
    }
}
