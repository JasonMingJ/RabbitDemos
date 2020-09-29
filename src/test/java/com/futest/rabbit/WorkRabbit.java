package com.futest.rabbit;

import com.fucode.utils.RabbitMQ;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName com.futest.rabbit.WorkRabbit
 * @Description: 一个交换机通过一个队列发送消息给多个消费端
 * @Author user
 * @Date 2020/9/28
 * @Version V1.0
 **/
public class WorkRabbit {
    private static final String QUEUE_NAME = "hello";

    @Test
    public void workPublish(){
        String [] argv = new String[]{"..."};
        try(Connection connection= RabbitMQ.getConnection();
            Channel channel = connection.createChannel())
        {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            String message = String.join(" ",argv);
            channel.basicPublish("", QUEUE_NAME, null,message.getBytes(StandardCharsets.UTF_8) );
            System.out.println("发送消息：" + message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void workerConsumer() throws IOException {
        Connection connection = RabbitMQ.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println("Work消费者开始等待消息。。。");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (deliveryConsumer, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("收到消息：" + message);
            try {
                /*模拟耗时的工作*/
                for (char m : message.toCharArray()) {
                    if (m == '.') {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }finally {
                System.out.println("Work完成");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, deliveryConsumer -> {});

        System.in.read();
    }

    @Test
    public void workerConsumer2() throws IOException {
        Connection connection = RabbitMQ.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println("Work消费者开始等待消息。。。");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (deliveryConsumer, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("收到消息：" + message);
            try {
                /*模拟耗时的工作*/
                for (char m : message.toCharArray()) {
                    if (m == '.') {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }finally {
                System.out.println("Work完成");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, deliveryConsumer -> {});

        System.in.read();
    }

    @Test
    public void workerConsumer3() throws IOException {
        Connection connection = RabbitMQ.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println("Work消费者开始等待消息。。。");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (deliveryConsumer, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("收到消息：" + message);
            try {
                /*模拟耗时的工作*/
                for (char m : message.toCharArray()) {
                    if (m == '.') {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }finally {
                System.out.println("Work完成");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, deliveryConsumer -> {});

        System.in.read();
    }


}
