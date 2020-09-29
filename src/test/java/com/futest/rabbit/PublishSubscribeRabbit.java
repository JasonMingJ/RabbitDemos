package com.futest.rabbit;

import com.fucode.utils.RabbitMQ;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName com.futest.rabbit.PublishSubscribeRabbit
 * @Description: TODO
 * @Author user
 * @Date 2020/9/28
 * @Version V1.0
 **/
public class PublishSubscribeRabbit {
    private static final String EXCHANGE_NAME = "logs";

    public void pubSubPublish(String[] argv) {
        try (Connection connection = RabbitMQ.getConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String message = argv.length < 1 ? "info:Hello World" : String.join("", argv);
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("交换机发送消息：" + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void sentExchangeLogs(){
        String[] arg = new String[]{"HelloExchange"};
        pubSubPublish(arg);
    }

    @Test
    public void receiveLogs(){
        try (Connection connection = RabbitMQ.getConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String queue = channel.queueDeclare().getQueue();
            //将交换机和队列绑定在一起
            channel.queueBind(queue, EXCHANGE_NAME, "");

            System.out.println("消费者正在等待log消息。。。");

            DeliverCallback deliverCallback = (deliveryConsumer, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("收到消息:" + message);
            };
            channel.basicConsume(queue, true, deliverCallback,deliveryConsumer -> {});
            System.in.read();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void receiveLogs2(){
        try (Connection connection = RabbitMQ.getConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String queue = channel.queueDeclare().getQueue();
            //将交换机和队列绑定在一起
            channel.queueBind(queue, EXCHANGE_NAME, "");

            System.out.println("消费者正在等待log消息。。。");

            DeliverCallback deliverCallback = (deliveryConsumer,delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("收到消息:" + message);
            };
            channel.basicConsume(queue, true, deliverCallback,deliveryConsumer -> {});
            System.in.read();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void receiveLogs3(){
        try (Connection connection = RabbitMQ.getConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String queue = channel.queueDeclare().getQueue();
            //将交换机和队列绑定在一起
            channel.queueBind(queue, EXCHANGE_NAME, "");

            System.out.println("消费者正在等待log消息。。。");

            DeliverCallback deliverCallback = (deliveryConsumer,delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("收到消息:" + message);
            };
            channel.basicConsume(queue, true, deliverCallback,deliveryConsumer -> {});
            System.in.read();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
