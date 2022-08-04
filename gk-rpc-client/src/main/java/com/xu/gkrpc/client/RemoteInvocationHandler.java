package com.xu.gkrpc.client;

import com.xu.gkrpc.Request;
import com.xu.gkrpc.Response;
import com.xu.gkrpc.ServiceDescriptor;
import com.xu.gkrpc.codec.Decoder;
import com.xu.gkrpc.codec.Encoder;
import com.xu.gkrpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 调用远程服务的代理类
 */
@Slf4j
public class RemoteInvocationHandler implements InvocationHandler {
    private Class clazz;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector transportSelector;

    public RemoteInvocationHandler(Class clazz, Encoder encoder, Decoder decoder, TransportSelector transportSelector) {
        this.clazz = clazz;
        this.encoder = encoder;
        this.decoder = decoder;
        this.transportSelector = transportSelector;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setServiceDescriptor(ServiceDescriptor.from(clazz, method));
        request.setParameters(args);
        Response resp = invokeRemote(request);
        if (resp == null || resp.getCode() != 0) {
            throw new IllegalStateException("fail to invoke remote: " + resp);
        }
        return resp.getData();
    }

    private Response invokeRemote(Request request) {
        TransportClient transportClient = null;
        Response resp = null;
        try {
            transportClient = transportSelector.select();
            byte[] outBytes = encoder.encode(request);
            InputStream receive = transportClient.write(new ByteArrayInputStream(outBytes));

            byte[] inBytes = new byte[receive.available()];
            IOUtils.readFully(receive, inBytes, 0, receive.available());
            resp = decoder.decode(inBytes, Response.class);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            resp.setCode(1);
            resp.setMessage("RpcClient got error: " + e.getClass() + " : " + e.getMessage() );
        } finally {
            if (transportClient != null) {
                transportSelector.release(transportClient);
            }
        }
        return resp;
    }
}
