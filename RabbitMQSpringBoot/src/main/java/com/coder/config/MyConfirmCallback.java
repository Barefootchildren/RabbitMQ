package com.coder.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MyConfirmCallback implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id=correlationData!=null?correlationData.getId():"";
        if(b){
            log.info("消息发送成功：correlationData({})",id);
        }else{
            log.info("消息发送失败：correlationData({}),cause({})",id,s);
        }
    }
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage){
        String message=new String(returnedMessage.getMessage().getBody());
        String exchange = returnedMessage.getExchange();
        String routingKey = returnedMessage.getRoutingKey();
        String replyText = returnedMessage.getReplyText();
        log.info("消息发送失败：消息内容为({}),被交换机({})退回,路由为({}),退回原因为({})",message,exchange,routingKey,replyText);
    }
}
