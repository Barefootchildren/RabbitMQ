package com.code.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static final String QUEUE_NAME = "hello";

        /**
     * 主函数，用于演示消息队列的基本使用流程
     * 该函数创建RabbitMQ连接，声明队列并发送一条测试消息
     *
     * @param args 命令行参数数组
     * @throws Exception 当连接或通信过程中发生错误时抛出异常
     */
    public static void main(String[] args) throws Exception {
        // 创建连接工厂并配置连接参数
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        // 建立连接并创建信道
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        // 声明队列，如果队列不存在则创建
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发送消息到指定队列
        String message="hello world";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("消息发送成功");
    }

}
