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
    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = Util.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        Map<String, String> routingKeyMap = new HashMap<>();
        routingKeyMap.put("start", "您购买的iphone100ProMax量子黑已发货");
        routingKeyMap.put("middle", "您购买的iphone100ProMax量子黑将由宇宙通快递护送");
        routingKeyMap.put("end", "您购买的iphone100ProMax量子黑已经抵达目的地，取件码为：NTR—001");
        Set<Map.Entry<String, String>> entries = routingKeyMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String routingKey = entry.getKey();
            String message = entry.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        }
    }

}
