package com.cheche.producer;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import static com.cheche.common.Commons.*;
/**
 *
 * 生产者
 * Created by user on 2016/2/23.
 */
public class SimpleProducer {

    private static Producer<Integer,String> producer;
    private final Properties properties = new Properties();

    public SimpleProducer() {
        properties.put("metadata.broker.list","localhost:9092");
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        producer = new Producer<Integer, String>(new ProducerConfig(properties));
    }

    private static KeyedMessage<Integer, String> senData(String topic,String data){
        return new KeyedMessage<Integer, String>(topic,data);
    }

    public static void main(String[] args) {
        SimpleProducer sp = new SimpleProducer();
        String topic = "mytopic";
//        StringBuilder builder = new StringBuilder();
        try {
            List<String> linkList = readLink("D:/tmp/all_carhome_config.txt");
            linkList.forEach(link -> {
                KeyedMessage<Integer, String> data = senData(topic, link);
                producer.send(data);
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
//        KeyedMessage<Integer,String> data = new KeyedMessage<Integer, String>(topic,builder.toString());
//        producer.send(data);
        producer.close();
    }
}
