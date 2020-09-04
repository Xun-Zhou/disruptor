package com.disruptor;

import com.disruptor.common.DisruptorQueue;
import com.disruptor.common.DisruptorQueueFactory;
import com.disruptor.common.ObjectEvent;
import com.disruptor.common.ObjectEventFactory;
import com.disruptor.customize.MyConsumer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadFactory;

@SpringBootTest(classes = DisruptorApplication.class)
class DisruptorApplicationTests {

    /**
     * 测试点对点
     */
    @Test
    void testWorkPoolQueue() {
        MyConsumer[] myConsumers = new MyConsumer[]{new MyConsumer("consumer1"), new MyConsumer("consumer2")};
        DisruptorQueue<String> disruptorQueue = DisruptorQueueFactory.getWorkPoolQueue(1024, false, myConsumers);
        for (int i = 0; i < 100; i++) {
            disruptorQueue.add("message" + i);
        }
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试发布订阅
     */
    @Test
    void testHandleEventsQueue() {
        MyConsumer[] myConsumers = new MyConsumer[]{new MyConsumer("consumer1"), new MyConsumer("consumer2")};
        DisruptorQueue<String> disruptorQueue = DisruptorQueueFactory.getHandleEventsQueue(1024, false, myConsumers);
        for (int i = 0; i < 100; i++) {
            disruptorQueue.add("message" + i);
        }
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试依赖关系 保证consumer1先消费
     */
    @Test
    void testRelation() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("DisruptorThreadPool").build();
        ObjectEventFactory<String> factory = new ObjectEventFactory<>();
        Disruptor<ObjectEvent<String>> disruptor = new Disruptor<>(factory, 1024, threadFactory, ProducerType.SINGLE, new SleepingWaitStrategy());
        MyConsumer myConsumer1 = new MyConsumer("consumer1");
        MyConsumer myConsumer2 = new MyConsumer("consumer2");
        disruptor.handleEventsWith(myConsumer2).then(myConsumer1);
        DisruptorQueue<String> disruptorQueue = DisruptorQueueFactory.getQueue(disruptor);
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                disruptorQueue.add("message" + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
