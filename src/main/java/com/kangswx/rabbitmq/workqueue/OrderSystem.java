package com.kangswx.rabbitmq.workqueue;

import com.google.gson.Gson;
import com.kangswx.rabbitmq.utils.RabbitMQConsts;
import com.kangswx.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OrderSystem {
    public static void main(String[] args) throws IOException {
        //TCP物理连接
        Connection connection = RabbitMQUtil.getConnection();

        //创建通信通道，相当于TCP的虚拟连接
        Channel channel = connection.createChannel();

        //创建队列，声明并创建一个队列，如果队列已经存在，则使用这个队列
        //第一个参数，对列名称  helloworld
        //第二个参数，是否持久话，false表示不持久化数据，MQ停掉后数据就会丢失
        //第三个参数，是否队列私有化，false表示所有的消费者都可以访问，true表示只有第一次拥有它的消费者才可以一直使用，其他消费者不能访问
        //第四个参数，是否自动删除，false连接停掉后不自动删除掉这个队列
        //第五个参数，其他额外的参数
        channel.queueDeclare(RabbitMQConsts.QUEUE_SMS, false, false, false, null);

        for (int i = 100; i < 199; i++) {
            SMS sms = new SMS("乘客" + i, "18628545" + i, "您已成功购买车票");
            String jsonSMS = new Gson().toJson(sms);
            channel.basicPublish("", RabbitMQConsts.QUEUE_SMS, null, jsonSMS.getBytes());
        }
        System.out.println("发送数据成功");
        try {
            channel.close();
            connection.close();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
