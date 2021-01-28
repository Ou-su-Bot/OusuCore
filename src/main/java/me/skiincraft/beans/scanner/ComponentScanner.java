package me.skiincraft.beans.scanner;

import me.skiincraft.beans.annotation.Component;
import me.skiincraft.beans.annotation.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ComponentScanner {

    private final ScanStorage scanStorage;
    private final Class<? extends Annotation>[] annotations;

    public ComponentScanner(Class<?>[] classes, Class<? extends Annotation>[] annotations){
        this.scanStorage = new ScanStorage(this);
        this.annotations = annotations;
        for (Class<?> clazz: classes){
            if (clazz.isAnnotation() || clazz.isEnum())
                continue;

            scanClass(clazz, annotations);
            scanFields(clazz, clazz.getDeclaredFields());
            scanMethods(clazz, clazz.getMethods()); // Not used
            scanConstructors(clazz, clazz.getConstructors()); // Not used
        }
    }

    public ComponentScanner(List<Class<?>> classes, Class<? extends Annotation>[] annotations){
        this(classes.toArray(new Class[0]), annotations);
    }

    private void scanClass(Class<?> clazz, Class<? extends Annotation>[] annotations){
        for (Class<? extends Annotation> annotation : annotations){
            if (clazz.isAnnotationPresent(annotation)) {
                scanStorage.getComponents().put(clazz, annotation);
            }
        }
    }

    private void scanFields(Type type, Field[] fields){
        for (Field field : fields){
            if (field.isAnnotationPresent(Inject.class)){
                if (Modifier.isStatic(field.getModifiers())) {
                    scanStorage.getFields().put(type, field);
                }
            }
        }
    }

    private void scanMethods(Type type, Method[] methods){
        for (Method method : methods){
            if (method.isAnnotationPresent(Inject.class)){
                scanStorage.getMethods().put(type, method);
            }
        }
    }

    private void scanConstructors(Type type, Constructor<?>[] constructors){
        for (Constructor<?> constructor : constructors){
            if (constructor.isAnnotationPresent(Inject.class)){
                scanStorage.getConstructors().put(type, constructor);
            }
        }
    }

    public void normalScan(Class<?> clazz){
        Class<? extends Annotation> annotation = Arrays.stream(clazz.getAnnotations())
                .map(Annotation::annotationType)
                .filter(an -> Arrays.stream(getAnnotations()).anyMatch(ann -> ann == an))
                .findFirst()
                .orElse(null);

        scanStorage.getComponents().put(clazz, (Objects.isNull(annotation) ? Component.class : annotation));
        scanFields(clazz, clazz.getDeclaredFields());
        scanMethods(clazz, clazz.getMethods());
        scanConstructors(clazz, clazz.getConstructors());
    }

    public Class<? extends Annotation>[] getAnnotations() {
        return annotations;
    }

    public ScanStorage getScanStorage() {
        return scanStorage;
    }
}
