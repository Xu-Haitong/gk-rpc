package com.xu.gkrpc.example;

import com.xu.gkrpc.client.RpcClient;

public class Client {
    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient();
        CalcService calcService = rpcClient.getProxy(CalcService.class);

        int r1 = calcService.add(1, 2);
        int r2 = calcService.minus(10, 8);

        System.out.println(r1);
        System.out.println(r2);
    }
}
