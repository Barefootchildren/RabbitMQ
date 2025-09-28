package com.code.consumer.Max;

import com.code.util.Util;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;


public class ConsumerTwo {
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        // 获取RabbitMQ通道连接
        Channel channel = Util.getChannel();
        System.out.println("死信消费者等待接受消息.....");
        // 定义消息接收回调函数，处理从死信队列接收到的消息
        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("死信消费者收到消息：" + message);
        };
        // 定义消费取消回调函数，当消费者被取消时执行
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("死信消费者取消消费");
        };
        // 启动消费者，监听死信队列并处理消息
        channel.basicConsume(DEAD_QUEUE,true, callback, cancelCallback);

    }
}
