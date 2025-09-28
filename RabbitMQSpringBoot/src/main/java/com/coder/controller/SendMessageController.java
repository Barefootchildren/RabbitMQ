package com.coder.controller;

import com.coder.config.DelayQueueOneConstant;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@RestController
@RequestMapping("/send")
public class SendMessageController {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @GetMapping("/{message}")
    public void sendMessage(@PathVariable String message){
        log.info("当前时间：{}发送消息：{}", LocalDateTime.now(),message);
        rabbitTemplate.convertAndSend(DelayQueueOneConstant.NORMAL_EXCHANGE,
                DelayQueueOneConstant.NORMAL_ROUTING_A,
                "消息来自TTL为10S的队列"+ message);
        rabbitTemplate.convertAndSend(DelayQueueOneConstant.NORMAL_EXCHANGE,
                DelayQueueOneConstant.NORMAL_ROUTING_B,
                "消息来自TTL为30S的队列"+ message);
    }
    @GetMapping("/{message}/{ttl}")
    public void sendMessage(@PathVariable String message,
                            @PathVariable String ttl){
        log.info("当前时间：{}发送消息：{}", LocalDateTime.now(),message);
        rabbitTemplate.convertAndSend(DelayQueueOneConstant.NORMAL_EXCHANGE,
                DelayQueueOneConstant.NORMAL_ROUTING_C,
                message,
                correlationData->{
                correlationData.getMessageProperties().setExpiration(ttl);
                return correlationData;
                });
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 C:{}",
                LocalTime.now(), ttl, message);
    }
    @GetMapping("/delay/{message}/{delayTime}")
    public void sendDelayMsg(@PathVariable String message,
                             @PathVariable Integer delayTime){
        rabbitTemplate.convertAndSend(DelayQueueOneConstant.DELAY_EXCHANGE,
                DelayQueueOneConstant.DELAY_ROUTING_KEY,
                message,
                correlationData->{
                correlationData.getMessageProperties().setDelay(delayTime);
                return correlationData;
                });
        log.info("当前时间：{},发送一条延迟{}毫秒的信息给队列 :{}",
                LocalTime.now(), delayTime, message);
    }
}
