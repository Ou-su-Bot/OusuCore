package me.skiincraft.beans;

import me.skiincraft.beans.annotation.Component;
import me.skiincraft.beans.impl.DefaultInjector;
import me.skiincraft.beans.scanner.ComponentScanner;
import me.skiincraft.beans.utils.ClassGetter;

import java.lang.annotation.Annotation;

public abstract class InjectorFactory {

    private static final InjectorFactory instance = new InjectorFactory() {};

    private InjectorFactory(){}

    public Injector createNewInjector(Class<?> mainClass){
        return createNewInjector(mainClass, Component.class);
    }

    @SafeVarargs
    public final Injector createNewInjector(Class<?> mainClass, Class<? extends Annotation>... customAnnotations){
        isComponentAnnotation(customAnnotations);
        return new DefaultInjector(new ComponentScanner(new ClassGetter(mainClass).getClassesByReference(), customAnnotations));
    }

    public void isComponentAnnotation(Class<? extends Annotation>[] annotations) {
        int componentAnnotations = 0;
        for (Class<? extends Annotation> annotation : annotations){
            if (annotation == Component.class) {
                componentAnnotations++;
                continue;
            }
            for (Annotation subAnnotation : annotation.getAnnotations()) {
                if (subAnnotation.annotationType() == Component.class){
                    componentAnnotations++;
                }
            }
        }

        if (componentAnnotations == annotations.length){
            return;
        }

        throw new RuntimeException("Alguma anotação não é anotação de componente.");
    }

    public static InjectorFactory getInstance() {
        return instance;
    }
}
