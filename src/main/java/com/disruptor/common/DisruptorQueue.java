package com.disruptor.common;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class DisruptorQueue<T> {

    private Disruptor<ObjectEvent<T>> disruptor;

    private RingBuffer<ObjectEvent<T>> ringBuffer;

    public DisruptorQueue(Disruptor<ObjectEvent<T>> disruptor) {
        this.disruptor = disruptor;
        this.ringBuffer = disruptor.getRingBuffer();
        this.disruptor.start();
    }

    public void add(T t) {
        if (t != null) {
            long sequence = this.ringBuffer.next();
            try {
                ObjectEvent<T> event = this.ringBuffer.get(sequence);
                event.setObj(t);
            } finally {
                this.ringBuffer.publish(sequence);
            }
        }
    }

    public void addAll(List<T> ts) {
        if (ts != null) {
            for (T t : ts) {
                if (t != null) {
                    this.add(t);
                }
            }
        }
    }

    public long cursor() {
        return this.disruptor.getRingBuffer().getCursor();
    }

    public void shutdown() {
        this.disruptor.shutdown();
    }
}
