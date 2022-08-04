package com.xu.gkrpc.client;

import com.xu.gkrpc.codec.Decoder;
import com.xu.gkrpc.codec.Encoder;
import com.xu.gkrpc.common.utils.ReflectionUtils;

import java.lang.reflect.Proxy;

public class RpcClient {
    private RpcClientConfig rpcClientConfig;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector transportSelector;

    public RpcClient() {
        this(new RpcClientConfig());
    }

    public RpcClient(RpcClientConfig rpcClientConfig) {
        this.rpcClientConfig = rpcClientConfig;

        encoder = ReflectionUtils.newInstance(rpcClientConfig.getEncoderClass());
        decoder = ReflectionUtils.newInstance(rpcClientConfig.getDecoderClass());
        transportSelector = ReflectionUtils.newInstance(rpcClientConfig.getSelectorClass());

        transportSelector.init(rpcClientConfig.getServers(), rpcClientConfig.getConnectionCount(), rpcClientConfig.getTransportClass());
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{clazz},
                new RemoteInvocationHandler(clazz, encoder, decoder, transportSelector)
        );
    }
}
