package com.xu.gkrpc.client;

import com.xu.gkrpc.Peer;
import com.xu.gkrpc.transport.TransportClient;

import java.util.List;

/**
 * 表示选择哪个 server 去连接
 */
public interface TransportSelector {
    /**
     * 初始化 selector
     * @param peers 可以连接的 server 端点信息
     * @param count client 和 server 可以建立多少个连接
     * @param clazz client 实现的 class
     */

    void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz);

    /**
     * 选择一个 transport 与 server 做交互
     */
    TransportClient select();

    /**
     * 释放用完的 client
     */
    void release(TransportClient transportClient);

    /**
     * 关闭 TransportSelector
     */
    void close();
}
