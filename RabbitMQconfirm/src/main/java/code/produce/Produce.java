package code.produce;

import code.util.Util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Produce {
    private static final int MESSAGE_COUNT = 1000;
    private static final String INDIVIDUAL_CONFIRM_QUEUE = "individual_confirm_queue";
    private static final String BATCH_CONFIRM_QUEUE = "batch_confirm_queue";
    private static final String ASYNC_CONFIRM_QUEUE = "async_confirm_queue";
    private static final String ASYNC_CONCURRENT_CONFIRM_QUEUE = "async_concurrent_confirm_queue";//58ms

    /**
     * 主函数，用于演示消息队列的基本使用流程
     * 该函数创建RabbitMQ连接，声明队列并发送一条测试消息
     *
     * @param args 命令行参数数组
     * @throws Exception 当连接或通信过程中发生错误时抛出异常
     */
    public static void main(String[] args) throws Exception {
        //confirmMessageIndividually();//206ms
        //batchConfirm();//40ms
        //asyncConfirm();//31ms
        asyncConcurrentConfirm();//33 ms
    }
    public static void confirmMessageIndividually() throws Exception {
        // 创建连接工厂并配置连接参数
        Channel channel = Util.getChannel();
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        // 声明队列，如果队列不存在则创建
        channel.queueDeclare(INDIVIDUAL_CONFIRM_QUEUE, false, false, false, null);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "message" + i;
            channel.basicPublish("",INDIVIDUAL_CONFIRM_QUEUE,null,message.getBytes());
            boolean b = channel.waitForConfirms();
            if (b) {
                System.out.println("message   " + i+"   消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时" + (end - begin) + "ms");
    }
    public static void batchConfirm() throws Exception {
        // 创建连接工厂并配置连接参数
        Channel channel = Util.getChannel();
        channel.confirmSelect();
        int batchSize = 100;
        int count = 0;
        long begin = System.currentTimeMillis();
        // 声明队列，如果队列不存在则创建
        channel.queueDeclare(BATCH_CONFIRM_QUEUE, false, false, false, null);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "message" + i;
            channel.basicPublish("",BATCH_CONFIRM_QUEUE,null,message.getBytes());
            count++;
            if (count == batchSize) {
                boolean b = channel.waitForConfirms();
                count=0;
                if (b) {
                    System.out.println("message第   " + i+"   条之前消息发送成功");
                }
            }

        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时" + (end - begin) + "ms");
    }
    public static void asyncConfirm() throws Exception {
        // 创建连接工厂并配置连接参数
        Channel channel = Util.getChannel();
        channel.confirmSelect();
        ConfirmCallback callback=(deliveryTag,multiple)->{
            System.out.println("message   " + deliveryTag+"   确认成功");
        };
        ConfirmCallback failCallback=(deliveryTag,multiple)->{
            System.out.println("message   " + deliveryTag+"   确认失败");
        };
        channel.addConfirmListener(callback,failCallback);
        // 声明队列，如果队列不存在则创建
        channel.queueDeclare(ASYNC_CONFIRM_QUEUE, false, false, false, null);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "message" + i;
            channel.basicPublish("",ASYNC_CONFIRM_QUEUE,null,message.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时" + (end - begin) + "ms");
    }
    public static void asyncConcurrentConfirm() throws Exception {
        // 创建连接工厂并配置连接参数
        Channel channel = Util.getChannel();
        channel.confirmSelect();
        ConcurrentSkipListMap<Long,String> map=new ConcurrentSkipListMap<>();
        ConfirmCallback callback=(deliveryTag,multiple)->{
            if( multiple){
                ConcurrentNavigableMap<Long, String> longStringConcurrentNavigableMap = map.headMap(deliveryTag, true);
                longStringConcurrentNavigableMap.clear();
            }else {
                map.remove(deliveryTag);
            }
            System.out.println("message   " + deliveryTag+"   确认成功");
        };
        ConfirmCallback failCallback=(deliveryTag,multiple)->{
            String s = map.get(deliveryTag);
            System.out.println("message   " + deliveryTag+"   确认失败，信息内容是："+s);
        };
        channel.addConfirmListener(callback,failCallback);
        // 声明队列，如果队列不存在则创建
        channel.queueDeclare(ASYNC_CONFIRM_QUEUE, false, false, false, null);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "message" + i;
            map.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("",ASYNC_CONFIRM_QUEUE,null,message.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时" + (end - begin) + "ms");
    }
}
