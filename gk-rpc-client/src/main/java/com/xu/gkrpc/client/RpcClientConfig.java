package com.xu.gkrpc.client;

import com.xu.gkrpc.Peer;
import com.xu.gkrpc.codec.Decoder;
import com.xu.gkrpc.codec.Encoder;
import com.xu.gkrpc.codec.JSONDecoder;
import com.xu.gkrpc.codec.JSONEncoder;
import com.xu.gkrpc.transport.HTTPTransportClient;
import com.xu.gkrpc.transport.TransportClient;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class RpcClientConfig {
    private Class<? extends TransportClient> transportClass = HTTPTransportClient.class;
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    private Class<? extends TransportSelector> selectorClass = RandomTransportSelector.class;

    /**
     * Peer 能够建立多少连接，默认只能建立一个连接
     */
    private int connectionCount = 1;

    private List<Peer> servers = Arrays.asList(new Peer("127.0.0.1", 9999));
}
