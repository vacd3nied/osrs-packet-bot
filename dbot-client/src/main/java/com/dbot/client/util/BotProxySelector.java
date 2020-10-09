package com.dbot.client.util;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class BotProxySelector extends ProxySelector {

    private final Map<String, String> proxyMap = new HashMap<>();

    @Override
    public List<Proxy> select(final URI uri) {
        final String proxy = proxyMap.getOrDefault(Thread.currentThread().getThreadGroup().getName(), null);

        if (proxy == null || proxy.equals("null")) {
            return Collections.singletonList(Proxy.NO_PROXY);
        }

        return Collections.singletonList(new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved(proxy, 9050)));
    }

    public void addMapping(final String username, final String proxyAddress) {
        proxyMap.put(username, proxyAddress);
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        System.out.println("Connection failed...");
    }
}
