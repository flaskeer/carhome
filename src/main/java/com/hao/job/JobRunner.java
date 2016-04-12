package com.hao.job;

/**
 * Created by user on 2016/4/12.
 */
public interface JobRunner {

    /**
     * 执行任务  抛出异常证明消费失败  返回null证明消费成功
     * @param job
     * @return
     * @throws Throwable
     */
    public Result run(Job job) throws Throwable;

}
