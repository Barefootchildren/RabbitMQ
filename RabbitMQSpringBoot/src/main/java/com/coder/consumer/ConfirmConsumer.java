package com.coder.consumer;

import com.coder.config.ConfirmQueueConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfirmConsumer {
    @RabbitListener(queues = ConfirmQueueConstant.CONFIRM_QUEUE)
    public void receive(Message message){
        String msg=new String(message.getBody());
        log.info("消费者接收到消息：{}",msg);
    }
}
