package com.hao.spider;

import com.hao.model.spider.Page;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by user on 2016/4/13.
 */
public interface Downloader {

    Page download(String url) throws IOException;

    default void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    default void sleep(int minSec,int maxSec) {
        int sec = ThreadLocalRandom.current().nextInt(minSec, maxSec);
        sleep(sec);
    }

}
