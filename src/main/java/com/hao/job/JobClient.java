package com.hao.job;

import com.hao.exception.JobSubmitException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *  任务客户端 用来提交任务
 * Created by user on
 * 2016/4/12.
 */
public class JobClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobClient.class);

    private void checkField(List<Job> jobs) {
        if (CollectionUtils.isEmpty(jobs)) {
            throw new JobSubmitException("job can not be null");
        }
        for (Job job : jobs) {
            if (job == null) {
                throw new JobSubmitException("job can not be null");
            } else {
                job.checkField();
            }
        }
    }

    public Response submitJob(final List<Job> jobs,SubmitType type) {
        checkField(jobs);
        final Response response = new Response();
        JobSubmitRequest jobSubmitRequest = new JobSubmitRequest();
        jobSubmitRequest.setJobs(jobs);
        if (SubmitType.ASYNC.equals(type)) {
            asyncSubmit(jobSubmitRequest);
        } else {
            syncSubmit(jobSubmitRequest);
        }
        response.setSuccess(true);
        return response;
    }

    private void syncSubmit(JobSubmitRequest jobSubmitRequest) {



    }

    private void asyncSubmit(JobSubmitRequest jobSubmitRequest) {
    }


    enum SubmitType {
        SYNC,
        ASYNC
    }

}
