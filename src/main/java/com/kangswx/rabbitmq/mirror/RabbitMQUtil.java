package com.kangswx.rabbitmq.mirror;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtil {

    private static ConnectionFactory connectionFactory;

    static {
        //ConnectionFactory创建MQ的物理连接
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.18.177");  //代理服务器地址
        connectionFactory.setPort(5673);          //代理服务器端口
        connectionFactory.setUsername("swkang");  //guest只能在本机进行访问,通过代理服务器发送消息时需要重新建立用户
        connectionFactory.setPassword("swkang");  //guest
        connectionFactory.setVirtualHost("/");    //虚拟主机
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);  //不需要显式的声明抛出
        }
        return connection;
    }
}
