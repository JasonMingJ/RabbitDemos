package com.futest.confirms;

import com.fucode.utils.RabbitMQ;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @ClassName TryTemplate
 * @Description: TODO
 * @Author user
 * @Date 2020/9/28
 * @Version V1.0
 **/
public class TryTemplate {
    public void tryT(){
        try (Connection connection = RabbitMQ.getConnection();
             Channel channel = connection.createChannel())
        {
            //TODO


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
