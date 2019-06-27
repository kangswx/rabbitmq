package com.kangswx.rabbitmq.mirror;

import com.kangswx.rabbitmq.utils.RabbitMQConsts;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {

        //TCP物理连接
        Connection connection = RabbitMQUtil.getConnection();

        //创建通道
        Channel channel = connection.createChannel();

        //绑定消息队列
        //第一个参数，对列名称  helloworld
        //第二个参数，是否持久话，false表示不持久化数据，MQ停掉后数据就会丢失
        //第三个参数，是否队列私有化，false表示所有的消费者都可以访问，true表示只有第一次拥有它的消费者才可以一直使用，其他消费者不能访问
        //第四个参数，是否自动删除，false连接停掉后不自动删除掉这个队列
        //第五个参数，其他额外的参数
        channel.queueDeclare(RabbitMQConsts.QUEUE_HELLO, false, false, false, null);

        //创建一个消息消费者
        //第一个参数，队列名称  helloworld
        //第二个参数，是否自动确认收到消息，false表示手动编写程序来确认消息，这是MQ推荐的做法
        //第三个参数，DefaultConsumer的实现类
        channel.basicConsume(RabbitMQConsts.QUEUE_HELLO, false, new Receiver(channel));

        //在消费者中不能关闭channel和connection
    }
}

class Receiver extends DefaultConsumer{

    private Channel channel;

    //重写构造函数，channel通道对象需要从外部传入，在handleDelivery中会用到
    public Receiver(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        /*super.handleDelivery(consumerTag, envelope, properties, body);*/

        String messageBody = new String(body);
        System.out.println("消费者接收到： " + messageBody);

        //签收消息，确认消息
        //第一个参数，envelope.getDeliveryTag()获取这个消息的TagId，是一个整数
        //第二个参数，false只确认签收当前的消息，true时，表示签收该消费者所有未签收的消息
        channel.basicAck(envelope.getDeliveryTag(), false);
    }

}
