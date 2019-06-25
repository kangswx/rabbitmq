package com.kangswx.rabbitmq.confirm;

import com.kangswx.rabbitmq.utils.RabbitMQConsts;
import com.kangswx.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 气象局(模拟生产者)
 */
public class WeatherBureau {

    public static void main(String[] args) throws IOException, TimeoutException {

        LinkedHashMap<String, String> area = new LinkedHashMap<>();
        area.put("china.shaanxi.xian.20190624", "中国陕西西安20190624天气数据");
        area.put("china.shandong.qingdao.20190624", "中国山东青岛20190624天气数据");
        area.put("china.henan.zhengzhou.20190624", "中国河南郑州20190624天气数据");
        area.put("us.cal.la.20190624", "美国加州洛杉矶20190624天气数据");

        area.put("china.shaanxi.xian.20190625", "中国陕西西安20190625天气数据");
        area.put("china.shandong.qingdao.20190625", "中国山东青岛20190625天气数据");
        area.put("china.henan.zhengzhou.20190625", "中国河南郑州20190625天气数据");
        area.put("us.cal.la.20190625", "美国加州洛杉矶20190625天气数据");

        //TCP物理连接
        Connection connection = RabbitMQUtil.getConnection();

        //创建通信通道，相当于TCP的虚拟连接
        Channel channel = connection.createChannel();

        //开启confirm监听模式
        channel.confirmSelect();
        //添加监听器的处理代码
        channel.addConfirmListener(new ConfirmListener() {
            //成功接收时处理的代码
            //第一个参数是消息在传递的过程中的唯一id，第二个参数是数据是否为批量接收(一般用不到)
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("消息成功投递，Tag:"+l);
            }

            //MQ拒收时处理的代码
            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("消息投递被拒收，Tag:"+l);
            }
        });

        //添加ReturnListener监听器
        /*channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {

            }
        });*/
        channel.addReturnListener(new ReturnCallback() {
            @Override
            public void handle(Return r) {
                System.err.println("=================");
                System.err.println("Return编码："+r.getReplyCode()+",描述信息："+r.getReplyText());
                System.err.println("交换机："+r.getExchange()+",路由key："+r.getRoutingKey());
                System.err.println("消息主题为："+new String(r.getBody()));
                System.err.println("=================");
            }
        });

        Iterator<Map.Entry<String, String>> iterator = area.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            //第一个参数，交换机
            //第二个参数，相当于数据筛选的条件
            //第三个参数，mandatory，为true时如果消息无法正常投递到交换机则return回生产者，为false时直接将消息丢弃
            //第四个参数，额外的设置属性
            //第五个参数，需要发送的消息的字节数组
            channel.basicPublish(RabbitMQConsts.EXCHANGE_WEATHER_TOPIC, next.getKey(), true, null, next.getValue().getBytes());
        }

        //让channel一直处于等待监听的状态
        /*channel.close();
        connection.close();*/
    }
}
