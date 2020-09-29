package com.futest.rabbit;

import com.fucode.utils.RabbitMQ;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName com.futest.rabbit.RoutingRabbit
 * @Description: TODO
 * @Author user
 * @Date 2020/9/28
 * @Version V1.0
 **/
public class RoutingRabbit {
    private static final String EXCHANGE_NAME2 = "logs2";
    private static final String QUEUE_NAME = "hello";

    /*交换机发送特定类型的错误信息*/
    public void emitLogDirect(String []argv) throws Exception {
        try(
                Connection connection = RabbitMQ.getConnection();
                Channel channel = connection.createChannel())
        {
            channel.exchangeDeclare(EXCHANGE_NAME2, BuiltinExchangeType.DIRECT);

            //特定级别的错误,arg[0]为错误级别
            String message = getMessage(argv);

            channel.basicPublish(EXCHANGE_NAME2,argv[0], null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送消息：" + message);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    private static String getMessage(String [] strings){
        return joinStrings(strings," ",0);
    }

    private static String joinStrings(String[] strings, String delimeter, int startIndex) {
        int length = strings.length;
        if (length==0) return "";
        if (length<= startIndex) return "";
        StringBuilder stringBuilder = new StringBuilder(strings[startIndex]);
        for (int i = 0; i < length;i++){
            stringBuilder.append(" ").append(strings[i]);
        }
        return stringBuilder.toString();
    }

    @Test
    public void emitDirectLogs() throws Exception {
        String[] s1 = new String[]{"error"};
        String[] s2 = new String[]{"info"};
        String[] s3 = new String[]{"debug"};
        emitLogDirect(s1);
        emitLogDirect(s2);
        emitLogDirect(s3);
    }

    /*接收指定级别的日志信息*/
    public void receiveLogDirect(String [] args) throws Exception {
        Connection connection = RabbitMQ.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME2,BuiltinExchangeType.DIRECT);

        if (args.length<1){
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }

        for (String severity : args) {
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME2,severity);
        }

        System.out.println("消费者正在等待消息。。。");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("收到消息：" + message);
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        System.in.read();
    }

    @Test
    public void receiveDirectLogs() throws Exception {
        String[] s = new String[]{"error","info"};
        receiveLogDirect(s);
    }

}
