package com.xu.gkrpc.example;

import com.xu.gkrpc.server.RpcServer;

public class Server {
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(CalcService.class, new CalcServiceImpl());
        rpcServer.start();
    }
}
