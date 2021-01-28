package me.skiincraft.beans;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Injector {

    void start() throws Exception;
    <T> void map(T instancedClass);
    <T> void inject(T instancedClass);
    <T> T getInstanceOf(Class<T> clazz);

    void commit();
    List<Object> getComponentsBy(Class<? extends Annotation> annotation);
    List<Object> getComponents();

    void onMap(Consumer<Object> consumer, Class<? extends Annotation> annotation);
    void onInject(Consumer<Object> consumer, Class<? extends Annotation> annotation);
    void onNewInstance(Function<Class<?>, Object> function, Class<? extends Annotation> annotation);
}
