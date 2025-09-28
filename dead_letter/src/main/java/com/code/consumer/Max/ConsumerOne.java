package com.code.consumer.Max;

import com.code.util.Util;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;


public class ConsumerOne {
    //声明普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //声明死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";
    //声明普通队列名称
    private static final String NORMAL_QUEUE = "normal_queue";
    //声明死信队列名称
    private static final String DEAD_QUEUE = "dead_queue";
    //声明死信路由名称
    private static final String NORMAL_ROUTING = "normal_routing";
    //声明普通路由名称
    private static final String DEAD_ROUTING = "dead_routing";

    public static void main(String[] args) throws Exception {
                // 获取RabbitMQ通道
        Channel channel = Util.getChannel();

        // 声明正常交换机和死信交换机，都使用直连类型
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明死信队列并绑定到死信交换机
        channel.queueDeclare(DEAD_QUEUE, false, false, false,null);
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTING);

        // 配置正常队列的死信交换机参数
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        objectObjectHashMap.put("x-dead-letter-routing-key", DEAD_ROUTING);
        objectObjectHashMap.put("x-max-length", 6);

        // 声明正常队列并绑定到正常交换机，同时设置死信转发规则
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,objectObjectHashMap);
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_ROUTING);

        System.out.println("普通消费者等待接受消息.....");

        // 定义消息处理回调函数
        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("普通消费者收到消息：" + message);
        };

        // 定义取消消费回调函数
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("普通消费者取消消费");
        };

        // 启动消费者，监听正常队列的消息
        channel.basicConsume(NORMAL_QUEUE, true,callback, cancelCallback);

    }
}
