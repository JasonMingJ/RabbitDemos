package com.futest.confirms;

import com.fucode.utils.RabbitMQ;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName BatchConfirmPublisher
 * @Description: TODO
 * @Author user
 * @Date 2020/9/28
 * @Version V1.0
 **/
public class BatchConfirmPublisher {

    @Test
    public void publish(){
        try (Connection connection = RabbitMQ.getConnection();
             Channel channel = connection.createChannel())
        {
            channel.confirmSelect();
            for (int i = 0; i < 1000 ; i++){
                String msg = "Hello-World" + i;
                channel.basicPublish("", "hello", null, msg.getBytes(StandardCharsets.UTF_8));
            }
            channel.waitForConfirmsOrDie();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
