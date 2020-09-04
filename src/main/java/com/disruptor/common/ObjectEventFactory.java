package com.disruptor.common;

import com.lmax.disruptor.EventFactory;

/**
 * @author Administrator
 */
public class ObjectEventFactory<T> implements EventFactory<ObjectEvent<T>> {

    @Override
    public ObjectEvent<T> newInstance() {
        return new ObjectEvent<>();
    }
}
