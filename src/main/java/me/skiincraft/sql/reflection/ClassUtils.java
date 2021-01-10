package me.skiincraft.sql.reflection;

import com.google.common.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassUtils<T> {

    private final Type type;

    public ClassUtils() {
        this.type = new TypeToken<T>(getClass()) {}.getType();
    }

    public ClassUtils(Class<T> cls) {
        this.type = cls;
    }

    public ClassUtils(Type type) {
        this.type = type;
    }

    public Class<T> getGenericClass(){
        return (Class<T>) type;
    }

    public <A extends Annotation> A containsAnnotation(Class<A> annotation){
        return getGenericClass().isAnnotationPresent(annotation) ? getGenericClass().getAnnotation(annotation): null;
    }

    public <A extends Annotation> Field getFieldWithAnnotation(Class<A> annotation){
        return getFieldsWithAnnotation(annotation).size() == 0 ? null : getFieldsWithAnnotation(annotation).get(0);
    }

    public <A extends Annotation> List<Field> getFieldsWithAnnotation(Class<A> annotation){
        return Arrays.stream(getGenericClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }

    public <A extends Annotation> List<Method> getMethodsWithAnnotation(Class<A> annotation){
        return Arrays.stream(getGenericClass().getDeclaredMethods()).filter(method ->
                method.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }
    public Type getType() {
        return type;
    }
}
