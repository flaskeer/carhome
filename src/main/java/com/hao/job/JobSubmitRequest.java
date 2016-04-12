package com.hao.job;

import java.util.List;

/**
 * Created by user on 2016/4/12.
 */
public class JobSubmitRequest {

    private List<Job> jobs;

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
