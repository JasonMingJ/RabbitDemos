package com.futest.confirms;

import com.fucode.utils.RabbitMQ;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName NormalConfPublisher
 * @Description: TODO
 * @Author user
 * @Date 2020/9/28
 * @Version V1.0
 **/
public class NormalConfPublisher {
    @Test
    public void publish(){

        try (Connection connection = RabbitMQ.getConnection();
             Channel channel = connection.createChannel())
        {
            //开启confirm
            channel.confirmSelect();

            //发送消息
            //第一个参数  定义交换机，使用默认的交换机 ，使用""
            //第二个参数   routingKey，路由key，这里写的是队列的名称
            //参数3：指定传递的消息所携带的properties，使用null。
            //参数4  要发送的消息的内容
            String content = "hello-world";
            channel.basicPublish("", "hello", null, content.getBytes(StandardCharsets.UTF_8));

            System.out.println("消息发布成功！");

            //判断消息是否到达交换机
            if (channel.waitForConfirms()) {
                System.out.println("消息未到达交换机！");
            } else {
                System.out.println("消息成功到达交换机！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
