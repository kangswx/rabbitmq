package com.kangswx.rabbitmq.workqueue;

import com.kangswx.rabbitmq.utils.RabbitMQConsts;
import com.kangswx.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class SMSSender2 {

    public static void main(String[] args) throws IOException {
        //TCP物理连接
        Connection connection = RabbitMQUtil.getConnection();

        //创建通信通道，相当于TCP的虚拟连接
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitMQConsts.QUEUE_SMS, false, false, false, null);

        //不设置的话，MQ会自定将所有的请求平均分配给所有的消费者
        //basicQos,MQ不再对消费者一次发送多个请求，而是消费者处理完一条消息后，再从队列中获取一天新的
        channel.basicQos(1);  //处理完一个再去一个新的

        channel.basicConsume(RabbitMQConsts.QUEUE_SMS, false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String jsonSMS = new String(body);
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("SMSSender2--短信发送成功" + jsonSMS);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
