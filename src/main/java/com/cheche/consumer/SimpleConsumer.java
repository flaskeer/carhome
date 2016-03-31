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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程消费
 * Created by user on 2016/2/23.
 */
public class SimpleConsumer {

    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executorService;

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

    public void testConsumer(int threadCount) throws IOException {
        Map<String,Integer> topicCount = Maps.newHashMap();
        topicCount.put(topic,1);
        Map<String, List<KafkaStream<byte[], byte[]>>> streams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> kafkaStreams = streams.get(topic);
        executorService = Executors.newFixedThreadPool(threadCount);

        for (KafkaStream<byte[], byte[]> stream : kafkaStreams) {
            executorService.submit(() -> {
                ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
                while(iterator.hasNext()){
                    String link = new String(iterator.next().message());
                    try {
                        ParserSpecificPage.parseSpecificPage(link,"D:/tmp/error_path.txt");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        consumer.shutdown();
        executorService.shutdown();
    }

    public static void main(String[] args) {
        String topic = "mytopic";
        SimpleConsumer consumer = new SimpleConsumer("localhost:2181","testgroup",topic);
        try {
            consumer.testConsumer(10);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
