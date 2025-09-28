package com.coder.consumer;

import com.coder.config.DelayQueueOneConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
public class Consumer {
    @RabbitListener(queues =DelayQueueOneConstant.DEAD_QUEUE)
    public void receiveD(Message message, Channel channel){
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到死信队列信息{}", LocalTime.now(), msg);
    }
}
