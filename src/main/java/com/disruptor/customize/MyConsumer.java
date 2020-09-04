package com.disruptor.customize;

import com.disruptor.common.AbstractDisruptorConsumer;

/**
 * 自定义消费者
 *
 * @author Administrator
 */
public class MyConsumer extends AbstractDisruptorConsumer<String> {

    private final String name;

    public MyConsumer(String name) {
        this.name = name;
    }

    @Override
    public void consume(String var1) {
        System.out.println(this.name + ":" + var1);
    }
}
