package me.skiincraft.sql;

import me.skiincraft.sql.exceptions.RepositoryException;
import me.skiincraft.sql.platform.SQLPlatform;
import me.skiincraft.sql.reflection.DynamicProxyInvocationHandler;
import me.skiincraft.sql.repository.BasicRepository;
import me.skiincraft.sql.repository.Repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BasicSQL {

    private static SQLPlatform sql;
    private static BasicSQL instance;
    private static final Map<Type, Repository<?, ?>> repositories = new HashMap<>();

    private BasicSQL() {
    }

    public <E extends Repository<T, ID>, T, ID> E registerRepository(Class<E> repositoryClass) throws RepositoryException {
        if (!repositoryClass.isInterface()){
            throw new RepositoryException("Você só pode registrar interfaces!");
        }
        Type[] types = null;
        for (Type genericInterface : repositoryClass.getGenericInterfaces()) {
            if (genericInterface instanceof ParameterizedType) {
                types = ((ParameterizedType) genericInterface).getActualTypeArguments();
            }
        }
        if (types == null){
            throw new RepositoryException("Os parâmetros genéricos não estão preenchidos corretamente.");
        }

        if (repositories.containsKey(types[0])) {
            return (E) repositories.get(types[0]);
        }
        E repository = (E) createNewInstance(repositoryClass, (Class<T>) types[0], (Class<ID>) types[1]);
        repositories.put(types[0], repository);

        return repository;
    }

    private <E extends Repository<T, ID>, T, ID> Object createNewInstance(Class<E> type, Class<T> t1, Class<ID> id) throws RepositoryException {
        Repository<T, ID> repository = new BasicRepository<>(t1, id);
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{type}, new DynamicProxyInvocationHandler<>(repository, type));
    }


    public static BasicSQL create(SQLPlatform platform) throws InstantiationException, SQLException {
        if (sql != null)
            throw new InstantiationException("Essa classe já foi instanciada!");

        sql = platform;
        sql.connect();
        return instance = new BasicSQL();
    }

    public static BasicSQL getInstance(){
        return instance;
    }

    public static SQLPlatform getSQL() {
        return sql;
    }
}
