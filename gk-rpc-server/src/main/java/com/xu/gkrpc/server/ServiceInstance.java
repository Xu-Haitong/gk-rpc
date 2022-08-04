package com.xu.gkrpc.server;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * 表示一个具体服务
 */
@Data
@AllArgsConstructor
public class ServiceInstance {
    /**
     * 服务提供的对象
     */
    private Object target;

    /**
     * 服务提供的对象暴露的方法
     */
    private Method method;
}
