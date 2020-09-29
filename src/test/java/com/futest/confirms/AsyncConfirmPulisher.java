package com.futest.confirms;

import com.fucode.utils.RabbitMQ;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName AsyncConfirmPulisher
 * @Description: TODO
 * @Author user
 * @Date 2020/9/28
 * @Version V1.0
 **/
public class AsyncConfirmPulisher {

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
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("消息发送成功，消息标识：" + deliveryTag + ",是否批量：" + multiple);
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("消息发送失败，消息标识：" + deliveryTag + ",是否批量：" + multiple);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
