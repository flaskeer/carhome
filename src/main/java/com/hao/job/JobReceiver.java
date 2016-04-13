package com.hao.job;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by user on 2016/4/13.
 */
public class JobReceiver {

    public void receive(JobSubmitRequest request) {
        List<Job> jobs = request.getJobs();
        if (CollectionUtils.isEmpty(jobs)) {
            return;
        }
        for (Job job : jobs) {
            addToQueue(job,request);
        }
    }

    private void addToQueue(Job job,JobSubmitRequest request) {


    }
}
