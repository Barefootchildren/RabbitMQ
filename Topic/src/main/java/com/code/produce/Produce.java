package com.code.produce;

import com.code.util.Util;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Produce {
    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = Util.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        Map<String, String> routingKeyMap = new HashMap<>();
        routingKeyMap.put("user.info.name", "被队列2接收");
        routingKeyMap.put("speed.twitter", "被队列2接收");
        routingKeyMap.put("sth.express.send", "被队列1接收");
        routingKeyMap.put("sth.express.name", "被队列1和2接收");
        routingKeyMap.put("speed.express.send", "被队列1和2接收");
        routingKeyMap.put("speed.send.name", "满足两个队列要求，但只会被队列2接收");
        routingKeyMap.put("slow.send.express", "不匹配任何绑定，不会被任何队列接收");
        routingKeyMap.put("speed.express.send.two", "四个单词满足队列2匹配");
        Set<Map.Entry<String, String>> entries = routingKeyMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String routingKey = entry.getKey();
            String message = entry.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            System.out.println("发送消息：" + message+"，路由键：" + routingKey);
        }
    }

}
