package com.kangswx.rabbitmq.routing;

import com.kangswx.rabbitmq.utils.RabbitMQConsts;
import com.kangswx.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

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

        Iterator<Map.Entry<String, String>> iterator = area.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            //第一个参数，交换机
            //第二个参数，相当于数据筛选的条件
            //第三个参数，额外的设置属性
            //第四个参数，需要发送的消息的字节数组
            channel.basicPublish(RabbitMQConsts.EXCHANGE_WEATHER_ROUTING, next.getKey(), null, next.getValue().getBytes());
        }

        channel.close();
        connection.close();
    }
}
