package com.disruptor.common;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class ObjectEvent<T> {

    private T obj;
}
