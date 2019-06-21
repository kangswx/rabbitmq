package com.kangswx.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtil {

    private static ConnectionFactory connectionFactory;

    static {
        //ConnectionFactory创建MQ的物理连接
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");  //ip地址
        connectionFactory.setPort(5672);         //端口
        connectionFactory.setUsername("guest");  //用户名
        connectionFactory.setPassword("guest");  //密码
        connectionFactory.setVirtualHost("/test"); //虚拟主机
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
