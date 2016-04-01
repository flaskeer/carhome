package com.hao.test.akka;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by user on 2016/4/1.
 */
public class MyUntypedActor extends UntypedActor{

    LoggingAdapter log = Logging.getLogger(getContext().system(),this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            log.info("receive string message:{}",message);
        } else {
            unhandled(message);
        }
    }
}
