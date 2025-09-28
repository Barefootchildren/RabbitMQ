package com.coder.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.WebEndpoint;
import java.util.HashMap;

@Configuration
public class QueueConfig {
    @Bean("normalExchange")
    public DirectExchange normalExchange() {
        return new DirectExchange(DelayQueueOneConstant.NORMAL_EXCHANGE);
    }
    @Bean("deadExchange")
    public DirectExchange deadExchange() {
        return new DirectExchange(DelayQueueOneConstant.DEAD_EXCHANGE);
    }
    @Bean("queueA")
    public Queue queueA(){
        HashMap<String, Object> map = new HashMap<>(3);
        map .put("x-dead-letter-exchange", DelayQueueOneConstant.DEAD_EXCHANGE);
        map .put("x-dead-letter-routing-key", DelayQueueOneConstant.DEAD_ROUTING);
        map.put("x-message-ttl", 10000);
        return QueueBuilder.durable(DelayQueueOneConstant.NORMAL_QUEUE_A).withArguments( map).build();
    }
    @Bean("queueB")
    public Queue queueB(){
        HashMap<String, Object> map = new HashMap<>(3);
        map .put("x-dead-letter-exchange", DelayQueueOneConstant.DEAD_EXCHANGE);
        map .put("x-dead-letter-routing-key", DelayQueueOneConstant.DEAD_ROUTING);
        map.put("x-message-ttl", 30000);
        return QueueBuilder.durable(DelayQueueOneConstant.NORMAL_QUEUE_B).withArguments( map).build();
    }
    @Bean("queueC")
    public Queue queueC(){
        HashMap<String, Object> map = new HashMap<>(3);
        map .put("x-dead-letter-exchange", DelayQueueOneConstant.DEAD_EXCHANGE);
        map .put("x-dead-letter-routing-key", DelayQueueOneConstant.DEAD_ROUTING);
        return QueueBuilder.durable(DelayQueueOneConstant.NORMAL_QUEUE_C).withArguments(map).build();
    }
    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DelayQueueOneConstant.DEAD_QUEUE).build();
    }
    @Bean
    public Binding queueABinding(@Qualifier("queueA")Queue queueA,
                                 @Qualifier("normalExchange")DirectExchange normalExchange){
        return BindingBuilder.bind(queueA).to(normalExchange).with(DelayQueueOneConstant.NORMAL_ROUTING_A);
    }
    @Bean
    public Binding queueBBinding(@Qualifier("queueB")Queue queueB,
                                 @Qualifier("normalExchange")DirectExchange normalExchange){
        return BindingBuilder.bind(queueB).to(normalExchange).with(DelayQueueOneConstant.NORMAL_ROUTING_B);
    }
    @Bean
    public Binding queueDBinding(@Qualifier("queueD")Queue queueD,
                                 @Qualifier("deadExchange")DirectExchange normalExchange){
        return BindingBuilder.bind(queueD).to(normalExchange).with(DelayQueueOneConstant.DEAD_ROUTING);
    }
    @Bean
    public Binding queueCBinding(@Qualifier("queueC")Queue queueC,
                                 @Qualifier("normalExchange")DirectExchange normalExchange){
        return BindingBuilder.bind(queueC).to(normalExchange).with(DelayQueueOneConstant.NORMAL_ROUTING_C);
    }
}
