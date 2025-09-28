package com.coder.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ConfirmQueueConfig {
    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(ConfirmQueueConstant.CONFIRM_QUEUE).build();
    }
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
        return new DirectExchange(ConfirmQueueConstant.CONFIRM_EXCHANGE);
    }
    @Bean
    public Binding confirmBinding(@Qualifier("confirmQueue") Queue queue,
                                 @Qualifier("confirmExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ConfirmQueueConstant.CONFIRM_ROUTING_KEY);
    }
    @Bean
    public Binding backupQueueBinding(@Qualifier("backupQueue")Queue backupQueue,
                                      @Qualifier("backupExchange")FanoutExchange backupExchange){
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }
    @Bean
    public Binding warningQueueBinding(@Qualifier("warningQueue")Queue warningQueue,
                                      @Qualifier("backupExchange")FanoutExchange backupExchange){
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
