package com.futest.rabbit;

import com.fucode.utils.RabbitMQ;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName com.futest.rabbit.TopicRabbit
 * @Description: TODO
 * @Author user
 * @Date 2020/9/28
 * @Version V1.0
 **/
public class TopicRabbit {

    private static final String EXCHANGE_NAME = "topic-exchange";

    /*
    * 队列只能接收其绑定的（# *）约定表达式的路由器，发给其他未被队列绑定的路由器的消息则不会被消费端监听*/
    @Test
    public void topicEmit() throws IOException {
        Connection connection = RabbitMQ.getConnection();
        Channel channel = connection.createChannel();

        // 创建exchange并指定绑定方式
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueBind("topic-queue-1",EXCHANGE_NAME,"*.red.*");
        channel.queueBind("topic-queue-2",EXCHANGE_NAME,"fast.#");
        channel.queueBind("topic-queue-3",EXCHANGE_NAME,"*.*.rabbit");

        // 发布消息到exchange，同时指定路由的规则
        channel.basicPublish(EXCHANGE_NAME,"fast.red.monkey",null,"快红猴".getBytes());
        channel.basicPublish(EXCHANGE_NAME,"slow.black.dog",null,"慢黑狗".getBytes());
        channel.basicPublish(EXCHANGE_NAME,"fast.white.cat",null,"快白兔".getBytes());

        System.out.println("消息发送完毕。");
    }

    @Test
    public void topicConsumer() throws IOException {
        Connection connection = RabbitMQ.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("topic-queue-1", true, false, false, null);
        channel.queueDeclare("topic-queue-2", true, false, false, null);
        channel.queueDeclare("topic-queue-3", true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("收到消息:" + msg );
        };

        System.out.println("消费者正在等待消息。。。");

        channel.basicConsume("topic-queue-1", true, deliverCallback, consumerTag -> {});
        channel.basicConsume("topic-queue-3", true, deliverCallback, consumerTag -> {});

        System.in.read();
    }

}
