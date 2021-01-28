/*
 * Esta classe foi retirada de um snipped code por github.com/ddopson
 * Foi modificada por mim para uso próprio.
 *
 * Caso queira mais detalhes: https://github.com/ddopson/java-class-enumerator
 */

package me.skiincraft.beans.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ClassGetter {

    private final Class<?> referenceClass;
    private final ClassLoader classLoader;

    public ClassGetter(Class<?> referenceClass, ClassLoader classLoader) {
        this.referenceClass = referenceClass;
        this.classLoader = classLoader;
    }

    public ClassGetter(Class<?> referenceClass) {
        this.referenceClass = referenceClass;
        this.classLoader = Thread.currentThread().getContextClassLoader();
    }

    public List<Class<?>> getClasses(String packageName, Class<? extends Annotation> annotation){
        return getClasses(packageName).stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    public ArrayList<Class<?>> getClassesByReference(){
        return getClasses(referenceClass.getPackage().getName());
    }

    public ArrayList<Class<?>> getClasses(String packageName) {
        ArrayList<Class<?>> classes = new ArrayList<>();
        CodeSource src = referenceClass.getProtectionDomain().getCodeSource();
        if (src != null) {
            processJarfile(src.getLocation(), packageName, classes);
        }
        return classes;
    }

    private Class<?> loadClass(String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
        	throw new RuntimeException("Unexpected ClassNotFoundException loading class '" + className + "'");
        }
    }

    private String replacePackageName(String packageName){
        return packageName.replace('.', '/');
    }

    private String replaceResourcePath(String resourcePath){
        return resourcePath.replace("%20", " ");
    }

    @SuppressWarnings("resource")
    private void processJarfile(URL resource, String pkgname, ArrayList<Class<?>> classes) {
        String jarPath = replaceResourcePath(resource.getPath())
                .replaceFirst("[.]jar[!].*", ".jar")
                .replaceFirst("file:", "");

        try {
            JarFile jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                String className = null;
                if (entryName.endsWith(".class") && entryName.startsWith(replacePackageName(pkgname))
                        && entryName.length() > (replacePackageName(pkgname).length() + "/".length())) {
                    className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
                }
                if (className != null) {
                    classes.add(loadClass(className));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }
    }
}