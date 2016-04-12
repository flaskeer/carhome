package com.hao.job;

import com.hao.enums.RequestCode;
import com.hao.exception.JobSubmitException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *  任务客户端 用来提交任务
 * Created by user on
 * 2016/4/12.
 */
public class JobClient {

    private JobTracker jobTracker;

    private static final Logger LOGGER = LoggerFactory.getLogger(JobClient.class);

    public JobClient(JobTracker jobTracker) {
        this.jobTracker = jobTracker;
    }

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
        Command requestCommand = Command.createRequestCommand(RequestCode.SUBMIT_JOB.code(),jobSubmitRequest);
        SubmitCallback submitCallback = new SubmitCallback() {
            @Override
            public void call(Command command) {
                if (command == null) {
                    response.setSuccess(false);
                    response.setMsg("submit job failed ");
                    LOGGER.warn("submit job failed: {} ",jobs);
                    return;
                }
                LOGGER.info("submit job success:{}",jobs);
                response.setSuccess(true);
            }
        };
        if (SubmitType.ASYNC.equals(type)) {
            asyncSubmit(requestCommand,submitCallback);
        } else {
            syncSubmit(requestCommand,submitCallback);
        }
        return response;
    }

    private void syncSubmit(Command requestCommand,SubmitCallback submitCallback) {
        submitCallback.call(jobTracker.invokeSync(requestCommand));
    }

    private void asyncSubmit(Command requestCommand,SubmitCallback submitCallback) {
        final CountDownLatch latch = new CountDownLatch(1);
        jobTracker.invokeAsync(requestCommand, new AsyncCallBack() {
            @Override
            public void operationComplete(ResponseFuture future) {
                try {
                    submitCallback.call(future.getCommand());
                } finally {
                    latch.countDown();
                }
            }
        });

    }


    enum SubmitType {
        SYNC,
        ASYNC
    }

}
