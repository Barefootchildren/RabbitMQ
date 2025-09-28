package com.code.produce.TTL;

import com.code.util.Util;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Produce {
    private static final String EXCHANGE_NAME = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = Util.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        AMQP.BasicProperties build = new AMQP.BasicProperties().builder().expiration("10000").build();
        String routingKey = "normal_routing";
        for (int i = 0; i < 10; i++) {
            String message = "message："+ i;
            channel.basicPublish(EXCHANGE_NAME, routingKey, build, message.getBytes());
        }
        }
    }

