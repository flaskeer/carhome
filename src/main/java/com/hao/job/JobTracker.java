package com.hao.job;

import com.google.common.collect.Queues;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by user on 2016/4/12.
 */
public class JobTracker {

    private LinkedBlockingQueue queue = Queues.newLinkedBlockingQueue();

    public void add(List<Job> jobs) throws InterruptedException {
        for (Job job : jobs) {
            if (queue.contains(job)) {

            } else {
                queue.put(job);
            }
        }
    }

}
