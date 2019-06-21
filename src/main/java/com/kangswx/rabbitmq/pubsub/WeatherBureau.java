package com.kangswx.rabbitmq.pubsub;

import com.kangswx.rabbitmq.utils.RabbitMQConsts;
import com.kangswx.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 气象局(模拟生产者)
 */
public class WeatherBureau {

    public static void main(String[] args) throws IOException, TimeoutException {
        //TCP物理连接
        Connection connection = RabbitMQUtil.getConnection();

        //创建通信通道，相当于TCP的虚拟连接
        Channel channel = connection.createChannel();

        String input = new Scanner(System.in).next();

        //第一个参数，交换机
        //第二个参数，队列名称，此处不需要
        //第三个参数，额外的设置属性
        //第四个参数，需要发送的消息的字节数组
        channel.basicPublish(RabbitMQConsts.EXCHANGE_WEATHER, "", null, input.getBytes());

        channel.close();
        connection.close();
    }
}
