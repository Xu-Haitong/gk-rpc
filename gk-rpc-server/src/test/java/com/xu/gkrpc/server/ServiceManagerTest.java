package com.xu.gkrpc.server;

import com.xu.gkrpc.Request;
import com.xu.gkrpc.ServiceDescriptor;
import com.xu.gkrpc.common.utils.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

public class ServiceManagerTest {

    ServiceManager serviceManager;

    @Before
    public void init() {
        serviceManager = new ServiceManager();
        TestInterface bean = new TestClass();
        serviceManager.register(TestInterface.class, bean);
    }

    @Test
    public void register() {
        TestInterface bean = new TestClass();
        serviceManager.register(TestInterface.class, bean);
    }

    @Test
    public void lookUp() {
        Method method = ReflectionUtils.getPublicMethods(TestInterface.class)[0];

        Request request = new Request();
        ServiceDescriptor serviceDescriptor = ServiceDescriptor.from(TestInterface.class, method);
        request.setServiceDescriptor(serviceDescriptor);

        ServiceInstance serviceInstance = serviceManager.lookUp(request);
        assertNotNull(serviceInstance);
    }
}