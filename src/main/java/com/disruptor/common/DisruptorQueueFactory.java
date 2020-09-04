package com.disruptor.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

/**
 * @author Administrator
 */
public class DisruptorQueueFactory {

    private DisruptorQueueFactory() {
    }

    /**
     * 创建"点对点模式"的操作队列，即同一事件会被一组消费者其中之一消费
     *
     * @param queueSize      队列长度
     * @param isMoreProducer 是否多生产者
     * @param consumers      消费者
     */
    @SafeVarargs
    public static <T> DisruptorQueue<T> getWorkPoolQueue(int queueSize, boolean isMoreProducer,
                                                         AbstractDisruptorConsumer<T>... consumers) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("DisruptorThreadPool").build();
        ObjectEventFactory<T> factory = new ObjectEventFactory<>();
        Disruptor<ObjectEvent<T>> disruptor = new Disruptor<>(factory, queueSize, threadFactory, isMoreProducer ? ProducerType.MULTI : ProducerType.SINGLE, new BlockingWaitStrategy());
        disruptor.handleEventsWithWorkerPool(consumers);
        return new DisruptorQueue<>(disruptor);
    }

    /**
     * 创建"发布订阅模式"的操作队列，即同一事件会被多个消费者并行消费
     *
     * @param queueSize      队列长度
     * @param isMoreProducer 是否多生产者
     * @param consumers      消费者
     */
    @SafeVarargs
    public static <T> DisruptorQueue<T> getHandleEventsQueue(int queueSize, boolean isMoreProducer,
                                                             AbstractDisruptorConsumer<T>... consumers) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("DisruptorThreadPool").build();
        ObjectEventFactory<T> factory = new ObjectEventFactory<>();
        Disruptor<ObjectEvent<T>> disruptor = new Disruptor<>(factory, queueSize, threadFactory, isMoreProducer ? ProducerType.MULTI : ProducerType.SINGLE, new SleepingWaitStrategy());
        disruptor.handleEventsWith(consumers);
        return new DisruptorQueue<>(disruptor);
    }

    /**
     * 直接通过传入的 Disruptor 对象创建操作队列（如果消费者有依赖关系的话可以用此方法）
     */
    public static <T> DisruptorQueue<T> getQueue(Disruptor<ObjectEvent<T>> disruptor) {
        return new DisruptorQueue<>(disruptor);
    }
}
