package com.futest.confirms;

import com.fucode.utils.RabbitMQ;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ReturnListener;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName ReturnConfirmPublisher
 * @Description: TODO
 * @Author user
 * @Date 2020/9/28
 * @Version V1.0
 **/
public class ReturnConfirmPublisher {

    @Test
    public void publish(){
        try (Connection connection = RabbitMQ.getConnection();
             Channel channel = connection.createChannel())
        {
            //TODO
            channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
                //当消息没有到达队列时执行
                System.out.println("消息： "+new String(body,StandardCharsets.UTF_8) + "没有送达队列！！！");
            });

            channel.confirmSelect();
            for (int i = 0; i < 1000 ; i++){
                String msg = "Hello-World" + i;
                // 在发送消息时，指定mandatory参数为true，当队列没有接收到消息时，执行returnListener回调
                channel.basicPublish("", "hello", true, null, msg.getBytes(StandardCharsets.UTF_8));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
