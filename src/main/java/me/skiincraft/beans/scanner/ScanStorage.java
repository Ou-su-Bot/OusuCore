package me.skiincraft.beans.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ScanStorage {

    private final ConcurrentHashMap<Type, Field> fields = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Type, Method> methods = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Type, Constructor<?>> constructors = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Class<?>, Class<? extends Annotation>> components = new ConcurrentHashMap<>();
    private final ComponentScanner scanner;

    public ScanStorage(ComponentScanner scanner) {
        this.scanner = scanner;
    }

    public ConcurrentHashMap<Type, Field> getFields() {
        return fields;
    }

    public ConcurrentHashMap<Type, Method> getMethods() {
        return methods;
    }

    public ConcurrentHashMap<Type, Constructor<?>> getConstructors() {
        return constructors;
    }

    public ConcurrentHashMap<Class<?>, Class<? extends Annotation>> getComponents() {
        return components;
    }

    public ComponentScanner getScanner() {
        return scanner;
    }
}
