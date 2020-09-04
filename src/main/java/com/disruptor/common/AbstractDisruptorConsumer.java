package com.disruptor.common;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * @author Administrator
 */
public abstract class AbstractDisruptorConsumer<T> implements EventHandler<ObjectEvent<T>>, WorkHandler<ObjectEvent<T>> {

    @Override
    public void onEvent(ObjectEvent<T> event, long l, boolean b) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(ObjectEvent<T> event) throws Exception {
        this.consume(event.getObj());
    }

    /**
     * 执行方法
     *
     * @param var1 参数
     */
    public abstract void consume(T var1);
}
