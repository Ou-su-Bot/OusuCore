package me.skiincraft.discord.core.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CoreUtils {
	
	public static void createPath(String... uri) throws IOException {
		for (String i : uri) {
			if (!Paths.get(i).toFile().exists()) {
				Files.createDirectories(Paths.get(i));
			}
		}
	}
	
	public static void addClassPathURL(File jar) throws Exception {
		Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
		method.setAccessible(true);
		method.invoke(Thread.currentThread().getContextClassLoader(), new Object[] { jar.toURI().toURL() });
	}

}
