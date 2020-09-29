package com.futest.rabbit;

import com.fucode.utils.RabbitMQ;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName com.futest.rabbit.HelloRabbitTests
 * @Description: 一个交换机经一个队列发送消息给一个消费端
 * @Author user
 * @Date 2020/9/28
 * @Version V1.0
 **/
public class HelloRabbitTests {

    private static final String QUEUE_NAME = "hello";

    @Test
    public void helloWorld(){

        try(
        Connection connection = RabbitMQ.getConnection();
        Channel channel = connection.createChannel()) {
            String msg = "Hello World!";

            // 参数1：指定exchange，使用""。
            // 参数2：指定路由的规则，使用具体的队列名称。
            // 参数3：指定传递的消息所携带的properties，使用null。
            // 参数4：指定发布的具体消息，byte[]类型
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("消息发布！");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void msgConsumer() throws IOException, TimeoutException {
        try(Connection connection = RabbitMQ.getConnection();
            Channel channel = connection.createChannel();)
        {
            // 声明队列
            //参数1：queue - 指定队列的名称
            //参数2：durable - 当前队列是否需要持久化（true）
            //参数3：exclusive - 是否排外（conn.close() - 当前队列会被自动删除，当前队列只能被一个消费者消费）
            //参数4：autoDelete - 如果这个队列没有消费者在消费，队列自动删除
            //参数5：arguments - 指定当前队列的其他信息
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);

            //消息的消费者
            // DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            //     @Override
            //     public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            //         System.out.println("收到消息："+ new String(body,StandardCharsets.UTF_8));
            //     }
            // };

            // channel.basicConsume(QUEUE_NAME, true,defaultConsumer);

            //消息消费
            //DeliveryCallback -- a callback in the form of an object that will buffer the messages until we're ready to use them.
            DeliverCallback deliverCallback = (deliverConsumer, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("收到消息：" + message);
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, deliverConsumer -> {});

            System.out.println("消费者开始监听队列！");

            System.in.read();
        }
    }

}
