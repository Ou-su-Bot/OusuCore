package me.skiincraft.sql.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxyInvocationHandler<T> implements InvocationHandler {

    private final T target;
    private final Class<?> proxyType;

    public DynamicProxyInvocationHandler(T target, Class<? extends T> proxyType) {
        this.target = target;
        this.proxyType = proxyType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(target, args);
        } catch (IllegalArgumentException e2) {
            throw new UnsupportedOperationException("Este método não é suportado.");
        }
    }

    public Class<?> getType() {
        return proxyType;
    }

    public T getTarget() {
        return target;
    }
}
