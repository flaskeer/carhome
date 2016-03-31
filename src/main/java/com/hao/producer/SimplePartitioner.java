package com.hao.producer;

import kafka.producer.Partitioner;

/**
 * Created by user on 2016/3/31.
 */
public class SimplePartitioner implements Partitioner{


    @Override
    public int partition(Object key, int numPartitions) {
        int partition = 0;
        String partitionKey = (String) key;
        int offset = partitionKey.lastIndexOf(".");
        if (offset > 0) {
            partition = Integer.parseInt(partitionKey.substring(offset + 1)) % numPartitions;
        }
        return partition;
    }
}
