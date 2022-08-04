package com.xu.gkrpc.server;

import com.xu.gkrpc.codec.Decoder;
import com.xu.gkrpc.codec.Encoder;
import com.xu.gkrpc.codec.JSONDecoder;
import com.xu.gkrpc.codec.JSONEncoder;
import com.xu.gkrpc.transport.HTTPTransportServer;
import com.xu.gkrpc.transport.TransportServer;
import lombok.Data;

/**
 * server 配置
 */
@Data
public class RpcServerConfig {
    private Class<? extends TransportServer> transportClass = HTTPTransportServer.class;
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    private int port = 9999;
}
