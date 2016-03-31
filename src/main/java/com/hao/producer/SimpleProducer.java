package com.hao.producer;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static com.hao.common.Commons.*;
/**
 *
 * 生产者
 * Created by user on 2016/2/23.
 */
public class SimpleProducer {

    private static Producer<String,String> producer;
    private static Random random;
    private final Properties properties = new Properties();

    public SimpleProducer() {
        properties.put("metadata.broker.list","localhost:9092");
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        properties.put("partitioner.class","com.cheche.producer.SimplePartitioner");
        properties.put("request.required.acks","1");
        producer = new Producer<String, String>(new ProducerConfig(properties));
    }

    private static KeyedMessage<String, String> senData(String topic,String data){
        String clientIP = "127.0.0." + random.nextInt(255);
        return new KeyedMessage<>(topic,clientIP,data);
    }

    public static void main(String[] args) {
        SimpleProducer sp = new SimpleProducer();
        String topic = "mytopic";
//        StringBuilder builder = new StringBuilder();
        try {
            List<String> linkList = readLink("D:/tmp/all_carhome_config.txt");
            linkList.forEach(link -> {
                KeyedMessage<String, String> data = senData(topic, link);
                producer.send(data);
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        producer.close();
    }
}
