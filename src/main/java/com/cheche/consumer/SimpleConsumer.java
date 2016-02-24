package com.cheche.consumer;

import com.cheche.parser.ParserSpecificPage;
import com.google.common.collect.Maps;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by user on 2016/2/23.
 */
public class SimpleConsumer {

    private final ConsumerConnector consumer;
    private final String topic;

    public SimpleConsumer(String zookeeper,String groupId,String topic) {
        Properties properties = new Properties();
        properties.put("zookeeper.connect",zookeeper);
        properties.put("group.id",groupId);
        properties.put("zookeeper.session.timeout.ms","500");
        properties.put("zookeeper.sync.time.ms","250");
        properties.put("auto.commit.interval.ms","1000");
        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
        this.topic = topic;
    }

    public void testConsumer() throws IOException {
        Map<String,Integer> topicCount = Maps.newHashMap();
        topicCount.put(topic,new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> streams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> kafkaStreams = streams.get(topic);
        for (KafkaStream<byte[], byte[]> stream : kafkaStreams) {
            ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
            while(iterator.hasNext()){
                String link = new String(iterator.next().message());
                ParserSpecificPage.parseSpecificPage(link,"D:/tmp/error_path.txt");
            }
        }
        if(consumer != null){
            consumer.shutdown();
        }
    }

    public static void main(String[] args) {
        String topic = "mytopic";
        SimpleConsumer consumer = new SimpleConsumer("localhost:2181","testgroup",topic);
        try {
            consumer.testConsumer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
