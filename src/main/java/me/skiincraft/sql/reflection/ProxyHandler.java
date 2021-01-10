package me.skiincraft.sql.reflection;

import me.skiincraft.sql.repository.Repository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ProxyHandler implements InvocationHandler {

    private final Repository<?, ?> repository;
    private final Method[] methods;
    private final Class<?> type;

    public ProxyHandler(Repository<?, ?> repository, Class<?> type) {
        this.repository = repository;
        this.methods = repository.getClass().getMethods();
        this.type = type;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method method1 = Arrays.stream(methods).filter(filterMethod -> filterMethod.getName().equals(method.getName())).findFirst().orElse(null);
        if (method.getName().equalsIgnoreCase("toString")){
            return type.getName() + "@" + Integer.toHexString(repository.hashCode());
        }
        if (method.getName().equalsIgnoreCase("getClass")){
            return type;
        }

        if (method1 != null){
            return method1.invoke(repository, args);
        }

        if (method.isDefault()){
            method.invoke(proxy, args);
        }

        throw new UnsupportedOperationException("Este metodo que você invocou não é suportado.");
    }

    public Class<?> getType() {
        return type;
    }

    public Repository<?, ?> getRepository() {
        return repository;
    }
}
