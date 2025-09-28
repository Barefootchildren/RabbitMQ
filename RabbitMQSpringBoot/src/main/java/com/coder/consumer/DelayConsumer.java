package com.coder.consumer;

import com.coder.config.DelayQueueOneConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
public class DelayConsumer {
    @RabbitListener(queues = DelayQueueOneConstant.DELAY_QUEUE)
    public void receiveDelay(Message message){
        String msg=new String(message.getBody());
        log.info("当前时间：{},收到延时队列信息{}", LocalTime.now(), msg);
    }
}
