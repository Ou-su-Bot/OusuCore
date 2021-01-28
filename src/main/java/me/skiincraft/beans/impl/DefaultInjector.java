package me.skiincraft.beans.impl;

import me.skiincraft.beans.Injector;
import me.skiincraft.beans.annotation.Inject;
import me.skiincraft.beans.proxy.ProxyHandler;
import me.skiincraft.beans.scanner.ComponentScanner;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultInjector implements Injector {

    private final ConcurrentHashMap<Class<? extends Annotation>, Consumer<Object>> onMapAnnotations = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<? extends Annotation>, Function<Class<?>, Object>> onNewInstanceAnnotations = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<? extends Annotation>, Consumer<Object>> onInjectAnnotations = new ConcurrentHashMap<>();

    public final List<Object> instances = new ArrayList<>();
    private final ComponentScanner scanner;
    private boolean isStarted;

    public DefaultInjector(ComponentScanner scanner){
        this.scanner = scanner;
    }

    public void start() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (isStarted){
            return;
        }
        isStarted = true;


        List<Class<?>> annotatedClasses = scanner.getScanStorage().getComponents().keySet()
                .stream()
                .filter(clazz -> !scanner.getScanStorage().getConstructors().containsKey(clazz))
                .filter(clazz -> getObject(clazz) == null)
                .collect(Collectors.toList());

        for (Class<?> clazz : annotatedClasses){
            instanceClass(clazz, scanner.getScanStorage().getComponents().get(clazz));
        }
        instances.forEach(this::inject);
        Enumeration<Constructor<?>> constructors = scanner.getScanStorage().getConstructors().elements();
        while (constructors.hasMoreElements()){
            Constructor<?> construtor = constructors.nextElement();
            if (Objects.nonNull(getObject(construtor.getDeclaringClass()))) {
                continue;
            }
            List<Object> parameters = new ArrayList<>();
            for (Parameter parameter : construtor.getParameters()){
                instances.stream()
                        .filter(object -> object.getClass() == parameter.getType())
                        .findFirst().ifPresent(parameters::add);
            }
            if (construtor.getParameterCount() == 0){
                instanceConstructor(construtor);
                continue;
            }

            if (parameters.size() != construtor.getParameters().length){
                continue;
            }
            instanceConstructor(construtor, parameters.toArray(new Object[0]));
        }
        Enumeration<Field> elements = scanner.getScanStorage().getFields().elements();
        instances.forEach(this::inject);
        while (elements.hasMoreElements()) {
            Field field = elements.nextElement();
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            Object object = getObject(field.getType());
            if (object != null) {
                field.setAccessible(true);
                field.set(null, object);
            }
        }
    }

    private Object getObject(Class<?> clazz){
        return instances.stream().filter(object -> object.getClass().isAssignableFrom(clazz)).findFirst().orElse(null);
    }

    private void instanceClass(Class<?> clazz, Class<? extends Annotation> annotation) throws IllegalAccessException, InstantiationException {
        Object object = null;
        if (getOnInstance().containsKey(annotation)) {
            object = getOnInstance().get(annotation).apply(clazz);
        }

        if (Objects.isNull(object)){
            object = clazz.newInstance();
        }
        if (getOnMap().containsKey(annotation)){
            getOnMap().get(annotation).accept(object);
        }

        instances.add(object);
    }

    private void instanceConstructor(Constructor<?> clazz, Object...parameters) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object object = clazz.newInstance(parameters);
        Class<? extends Annotation> annotation = getAnnotationBy(clazz.getDeclaringClass());
        if (annotation != null && getOnMap().containsKey(annotation)){
            getOnMap().get(annotation).accept(object);
        }
        instances.add(object);
    }

    @Override
    public <T> void map(T instancedClass) {
        if (instancedClass instanceof Type) {
            throw new UnsupportedOperationException("A classe precisa estar instanciada para ser mapeada!");
        }
        scanner.normalScan(instancedClass.getClass());
        Class<? extends Annotation> annotation = getAnnotationBy(instancedClass.getClass());
        if (annotation != null && getOnMap().containsKey(annotation)){
            getOnMap().get(annotation).accept(instancedClass);
        }
        instances.add(instancedClass);
    }

    @Override
    public <T> void inject(T instancedClass) {
        if (instancedClass instanceof Type) {
            throw new UnsupportedOperationException("A classe precisa estar instanciada para ser injetada!");
        }

        Class<? extends Annotation> annotation = getAnnotationBy(instancedClass.getClass());
        if (annotation != null && getOnInject().containsKey(annotation)){
            getOnInject().get(annotation).accept(instancedClass);
        }
        injectInFields(instancedClass);
    }

    @Nullable
    public <T> T getInstanceOf(Class<T> clazz){
        return instances.stream().filter(object -> clazz.isAssignableFrom(object.getClass()) || object.getClass() == clazz).map(object -> (T) object).findFirst().orElse(null);
    }

    private Class<? extends Annotation> getAnnotationBy(Class<?> clazz){
        if (scanner.getScanStorage().getComponents().containsKey(clazz)){
            return scanner.getScanStorage().getComponents().get(clazz);
        }
        return null;
    }

    @Override
    public void commit() {
        instances.forEach(this::inject);
    }

    private <T> void injectInFields(T instancedClass) {
        Field[] fields = instancedClass.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }
            Object inject = instances.stream().filter(clazz -> {
                if (Proxy.isProxyClass(clazz.getClass())) {
                    if (Proxy.getInvocationHandler(clazz).getClass().isAssignableFrom(ProxyHandler.class)) {
                        ProxyHandler<?> handler = (ProxyHandler<?>) Proxy.getInvocationHandler(clazz);
                        return handler.getType() == field.getType();
                    }
                }

                return clazz.getClass().isAssignableFrom(field.getType()) || field.getType() == clazz;
            }).findFirst().orElse(null);
            if (Objects.isNull(inject)){
                continue;
            }
            try {
                field.setAccessible(true);
                field.set(instancedClass, inject);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Object> getComponentsBy(Class<? extends Annotation> annotation) {
        return instances.stream().filter(object -> object.getClass().isAnnotationPresent(annotation)).collect(Collectors.toList());
    }

    @Override
    public List<Object> getComponents() {
        return instances;
    }

    @Override
    public void onMap(Consumer<Object> consumer, Class<? extends Annotation> annotation) {
        onMapAnnotations.put(annotation, consumer);
    }

    public void onNewInstance(Function<Class<?>, Object> function, Class<? extends Annotation> annotation){
        onNewInstanceAnnotations.put(annotation, function);
    }

    @Override
    public void onInject(Consumer<Object> consumer, Class<? extends Annotation> annotation) {
        onInjectAnnotations.put(annotation, consumer);
    }

    public ConcurrentHashMap<Class<? extends Annotation>, Function<Class<?>, Object>> getOnInstance(){
        return onNewInstanceAnnotations;
    }

    public ConcurrentHashMap<Class<? extends Annotation>, Consumer<Object>> getOnMap(){
        return onMapAnnotations;
    }

    public ConcurrentHashMap<Class<? extends Annotation>, Consumer<Object>> getOnInject(){
        return onInjectAnnotations;
    }
}
