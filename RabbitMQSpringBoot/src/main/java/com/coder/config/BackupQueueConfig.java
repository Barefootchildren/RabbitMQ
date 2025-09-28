package com.coder.config;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class BackupQueueConfig {
    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(ConfirmQueueConstant.CONFIRM_QUEUE).build();
    }
    @Bean("backupQueue")
    public Queue backupQueue(){
        return QueueBuilder.durable(ConfirmQueueConstant.BACKUP_QUEUE).build();
    }
    @Bean("warningQueue")
    public Queue warningQueue(){
        return QueueBuilder.durable(ConfirmQueueConstant.WARING_QUEUE).build();
    }
    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return new FanoutExchange(ConfirmQueueConstant.BACKUP_EXCHANGE);
    }
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
        return ExchangeBuilder.directExchange(ConfirmQueueConstant.CONFIRM_EXCHANGE)
                .durable(true)
                .withArgument("alternate-exchange", ConfirmQueueConstant.BACKUP_EXCHANGE)
                .build();
    }
    @Bean
    public Binding confirmBinding(@Qualifier("confirmQueue") Queue confirmQueue,
                                  @Qualifier("confirmExchange")DirectExchange confirmExchange){
        return BindingBuilder
                .bind(confirmQueue)
                .to(confirmExchange)
                .with(ConfirmQueueConstant.CONFIRM_ROUTING_KEY);
    }
    @Bean
    public Binding backupQueueBinding(@Qualifier("warningQueue")Queue warningQueue,
                                      @Qualifier("backupExchange")FanoutExchange backupExchange){
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
    @Bean
    public Binding warningQueueBinding(@Qualifier("backupQueue")Queue backupQueue,
                                      @Qualifier("backupExchange")FanoutExchange backupExchange){
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }
}
