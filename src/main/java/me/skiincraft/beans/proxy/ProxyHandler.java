package me.skiincraft.beans.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxyHandler<T> implements InvocationHandler {

    private final T originalObject;
    private final Method[] methods;
    private final Class<?> proxyType;

    public ProxyHandler(T originalObject, Class<?> proxyType) {
        this.originalObject = originalObject;
        this.methods = originalObject.getClass().getMethods();
        this.proxyType = proxyType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method method1 = Arrays.stream(methods)
                .filter(filterMethod -> filterMethod.getName().equals(method.getName()))
                .filter(filterMethod -> filterParameters(args, filterMethod.getParameterTypes()))
                .findFirst()
                .orElse(null);
        if (method.getName().equalsIgnoreCase("toString")){
            return proxyType.getName() + "@" + Integer.toHexString(proxyType.hashCode());
        }
        if (method.getName().equalsIgnoreCase("getClass")){
            return proxyType;
        }

        if (method1 != null){
            try {
                return method1.invoke(getOriginalObject(), args);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if (method.isDefault()){
            method.invoke(proxy, args);
        }

        throw new UnsupportedOperationException("Este metodo que você invocou não é suportado.");
    }

    public Class<?> getType() {
        return proxyType;
    }

    public T getOriginalObject() {
        return originalObject;
    }

    public boolean filterParameters(Object[] args, Class<?>[] parameters) {
        if ((args == null || args.length == 0) && parameters.length == 0){
            return true;
        }
        AtomicInteger integer = new AtomicInteger(0);
        return Arrays.stream(args).map(arg -> {
            if (arg instanceof Type){
                return (Class<?>) arg;
            }
            return arg.getClass();
        }).filter(arg -> arg.isAssignableFrom(parameters[integer.getAndIncrement()]))
                .allMatch((arg) -> Arrays.stream(parameters).anyMatch(parameter -> parameter == arg));
    }
}
