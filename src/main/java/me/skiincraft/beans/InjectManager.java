package me.skiincraft.beans;

import me.skiincraft.beans.annotation.*;
import me.skiincraft.beans.utils.ClassGetter;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.command.Command;
import me.skiincraft.discord.core.common.EventListener;
import me.skiincraft.discord.core.exception.InjectException;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.sql.BasicSQL;
import me.skiincraft.sql.exceptions.RepositoryException;
import me.skiincraft.sql.repository.Repository;
import me.skiincraft.sql.reflection.ProxyHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InjectManager {

    private static final List<Object> classes = new ArrayList<>();
    private static final InjectManager instance = new InjectManager();

    private InjectManager() {
    }

    public void mapClasses(OusuPlugin plugin) {
        String mainPackage = plugin.getClass().getPackage().getName();
        ArrayList<Class<?>> classes = new ClassGetter(plugin.getClass()).getClasses(mainPackage);
        for (Class<?> clazz : classes) {
            if (clazz.isEnum() || clazz.isAnnotation())
                continue;

            if (clazz.isInterface()) {
                if (clazz.isAnnotationPresent(RepositoryMap.class)) {
                    repositoryRegister(clazz);
                }
                continue;
            }

            if (clazz.isAnnotationPresent(CommandMap.class)) {
                commandRegister(clazz);
            }
            if (clazz.isAnnotationPresent(Event.class)) {
                eventRegister(clazz);
            }
            if (clazz.isAnnotationPresent(Component.class)) {
                registerComponent(clazz);
            }
        }
    }

    private void repositoryRegister(Class<?> clazz) {
        if (Repository.class.isAssignableFrom(clazz)) {
            try {
                Class<? extends Repository> repository = clazz.asSubclass(Repository.class);
                InjectManager.classes.add(BasicSQL.getInstance().registerRepository(repository));
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerComponent(Class<?> clazz) {
        try {
            InjectManager.classes.add(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            OusuCore.getLogger().error(String.format("Não foi possível instanciar um componente (%s)", clazz.getSimpleName()), e);
        }
    }

    private void commandRegister(Class<?> clazz) {
        if (Command.class.isAssignableFrom(clazz)) {
            try {
                Command command = clazz.asSubclass(Command.class).newInstance();
                OusuCore.registerCommand(command);
                InjectManager.classes.add(command);
            } catch (InstantiationException | IllegalAccessException e) {
                OusuCore.getLogger().error(String.format("Não foi possível instanciar um comando (%s)", clazz.getSimpleName()), e);
            }
        }
    }

    private void eventRegister(Class<?> clazz) {
        if (EventListener.class.isAssignableFrom(clazz)) {
            try {
                EventListener eventListener = clazz.asSubclass(EventListener.class).newInstance();
                OusuCore.registerListener(eventListener);
                InjectManager.classes.add(eventListener);
            } catch (InstantiationException | IllegalAccessException e) {
                OusuCore.getLogger().error(String.format("Não foi possível instanciar um evento (%s)", clazz.getSimpleName()), e);
            }
        }
    }

    public <T> void injectClass(T instancedClass) {
        for (Field field : instancedClass.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }
            Object inject = classes.stream().filter(clazz -> {
                if (Proxy.isProxyClass(clazz.getClass())) {
                    if (Proxy.getInvocationHandler(clazz) instanceof ProxyHandler) {
                        ProxyHandler handler = (ProxyHandler) Proxy.getInvocationHandler(clazz);
                        return handler.getType() == field.getType();
                    }
                }
                return clazz.getClass().isAssignableFrom(field.getType());
            }).findFirst().orElse(null);

            if (Objects.isNull(inject))
                throw new InjectException(String.format("Não existe nenhum componente(%s) instanciado." +
                        "%n1. Verifique se a classe está com uma anotação." +
                        "%n2. Verifique se este componente contém uma implementação." +
                        "%n3. @Inject não pode ser usado em classes que não foram mapeadas", field.getType().getSimpleName()));

            field.setAccessible(true);
            try {
                field.set(instancedClass, inject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> void map(T item) {
        if (!item.getClass().isInstance(item)) {
            throw new UnsupportedOperationException("A classe precisa estar instanciada para ser mapeada!");
        }
        InjectManager.classes.add(item);
        refreshInjects();
    }

    public void refreshInjects() {
        InjectManager.classes.forEach(this::injectClass);
    }

    public static InjectManager getInstance() {
        return instance;
    }
}
