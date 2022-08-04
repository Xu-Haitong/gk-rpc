package com.xu.gkrpc;

import lombok.Data;

/**
 * 表示 RPC 的一个请求
 */
@Data
public class Request {
    private ServiceDescriptor serviceDescriptor;
    private Object[] parameters;
}
