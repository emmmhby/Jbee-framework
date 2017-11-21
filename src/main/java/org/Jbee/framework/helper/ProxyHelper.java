package org.Jbee.framework.helper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHelper implements InvocationHandler{
    private Class<?> target;

    public ProxyHelper(Class<?> target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
