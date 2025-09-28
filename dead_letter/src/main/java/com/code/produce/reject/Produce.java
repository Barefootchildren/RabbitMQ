package com.code.produce.reject;

import com.code.util.Util;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.sun.xml.internal.ws.util.StringUtils;

public class Produce {
    private static final String EXCHANGE_NAME = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = Util.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String routingKey = "normal_routing";
        for (int i = 0; i < 10; i++) {
            String message = Integer.toString(i);
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            System.out.println("生产者发送消息：" + message);
        }
        }
    }

