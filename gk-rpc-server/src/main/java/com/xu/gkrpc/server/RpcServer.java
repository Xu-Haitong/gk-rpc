package com.xu.gkrpc.server;

import com.xu.gkrpc.Request;
import com.xu.gkrpc.Response;
import com.xu.gkrpc.codec.Decoder;
import com.xu.gkrpc.codec.Encoder;
import com.xu.gkrpc.common.utils.ReflectionUtils;
import com.xu.gkrpc.transport.RequestHandler;
import com.xu.gkrpc.transport.TransportServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class RpcServer {
    private RpcServerConfig rpcServerConfig;
    private TransportServer transportServer;
    private Encoder encoder;
    private Decoder decoder;
    private ServiceManager serviceManager;
    private ServiceInvoker serviceInvoker;
    private RequestHandler handler = new RequestHandler() {
        @Override
        public void onRequest(InputStream receive, OutputStream toResp) {
            Response response = new Response();
            try {
                byte[] inBytes = new byte[receive.available()];
                IOUtils.readFully(receive, inBytes, 0, receive.available());
                Request request = decoder.decode(inBytes, Request.class);
                log.info("get request: {}", request);

                ServiceInstance serviceInstance = serviceManager.lookUp(request);
                Object ret = serviceInvoker.invoke(serviceInstance, request);
                response.setData(ret);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
                response.setCode(1);
                response.setMessage("RpcServer got error" + e.getClass().getName() +
                        " : " +
                        e.getMessage());
            } finally {
                byte[] outBytes = encoder.encode(response);
                try {
                    toResp.write(outBytes);
                    log.info("response client");
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    };

    public RpcServer() {
        this(new RpcServerConfig());
    }

    public RpcServer(RpcServerConfig rpcServerConfig) {
        this.rpcServerConfig = rpcServerConfig;

        // net
        this.transportServer = ReflectionUtils.newInstance(this.rpcServerConfig.getTransportClass());
        this.transportServer.init(this.rpcServerConfig.getPort(), this.handler);

        // codec
        this.encoder = ReflectionUtils.newInstance(this.rpcServerConfig.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(this.rpcServerConfig.getDecoderClass());

        // service
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();
    }

    public <T> void register(Class<T> interfaceClass, T bean) {
        serviceManager.register(interfaceClass, bean);
    }

    public void start() {
        this.transportServer.start();
    }

    public void stop() {
        this.transportServer.stop();
    }
}
