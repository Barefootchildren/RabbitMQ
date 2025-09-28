package com.coder.config;

public class DelayQueueOneConstant {

    /**
     * 死信交换机实现延迟消息
     */

    //普通交换机名称
    public static final String NORMAL_EXCHANGE = "ne";
    //死信交换机名称
    public static final String DEAD_EXCHANGE = "de";
    //普通队列名称
    public static final String NORMAL_QUEUE_A = "nqA";
    public static final String NORMAL_QUEUE_B = "nqB";
    //死信队列名称
    public static final String DEAD_QUEUE = "dq";
    //普通队列路由
    public static final String NORMAL_ROUTING_A = "nrA";
    public static final String NORMAL_ROUTING_B = "nrB";
    //死信队列路由
    public static final String DEAD_ROUTING = "dr";
    public static final String NORMAL_QUEUE_C = "nqC";

    public static final String NORMAL_ROUTING_C = "nrC";
    public static final String DELAY_QUEUE = "delay.queue";
    public static final String DELAY_EXCHANGE = "delay.exchange";
    public static final String DELAY_ROUTING_KEY = "delay.routingKey";
    //交换机类型为延迟交换机
    public static final String DELAY_EXCHANGE_TYPE = "x-delayed-message";
}
