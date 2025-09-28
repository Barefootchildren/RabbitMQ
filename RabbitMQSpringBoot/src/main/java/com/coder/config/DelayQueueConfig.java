package com.coder.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
@Configuration
public class DelayQueueConfig {
    @Bean("delayQueue")
    public Queue dealyQueue(){
        return QueueBuilder.durable(DelayQueueOneConstant.DELAY_QUEUE).build();
    }

    @Bean("delayExchange")
    public CustomExchange delayExchange(){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("x-delayed-type", "direct");
        return new CustomExchange(DelayQueueOneConstant.DELAY_EXCHANGE,
                DelayQueueOneConstant.DELAY_EXCHANGE_TYPE,
                true,false,hashMap);
    }

    @Bean
    public Binding delayBinding(@Qualifier("delayQueue")Queue delayQueue,
                                @Qualifier("delayExchange")CustomExchange customExchange){
        return BindingBuilder.bind(delayQueue)
                .to(customExchange)
                .with(DelayQueueOneConstant.DELAY_ROUTING_KEY)
                .noargs();
    }
}
