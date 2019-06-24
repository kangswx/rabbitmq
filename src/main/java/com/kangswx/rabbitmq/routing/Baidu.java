package com.kangswx.rabbitmq.routing;

import com.kangswx.rabbitmq.utils.RabbitMQConsts;
import com.kangswx.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 百度(模拟消费者)
 */
public class Baidu {

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
        channel.queueDeclare(RabbitMQConsts.QUEUE_BAIDU, false, false, false, null);

        //绑定队列交换机
        //第一个参数，队列名称
        //第二个参数，交换机名称
        //第三个参数，路由key，此处需要设置路由规则
        channel.queueBind(RabbitMQConsts.QUEUE_BAIDU, RabbitMQConsts.EXCHANGE_WEATHER_ROUTING, "china.shandong.qingdao.20190624");
        channel.queueBind(RabbitMQConsts.QUEUE_BAIDU, RabbitMQConsts.EXCHANGE_WEATHER_ROUTING, "china.shandong.qingdao.20190625");

        //每次只去一条消息进行消费
        channel.basicQos(1);

        channel.basicConsume(RabbitMQConsts.QUEUE_BAIDU, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("百度收到气象信息----》"+new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }

}
