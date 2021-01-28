package me.skiincraft.discord.core.utils;

import me.skiincraft.discord.core.CoreStarter;
import me.skiincraft.discord.core.configuration.OusuConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CoreUtils {

	public static void createPath(String... uri) {
		try {
			for (String i : uri) {
				if (!Paths.get(i).toFile().exists()) {
					Files.createDirectories(Paths.get(i));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createPath(String main, String[] uri) {
		try {
			for (String i : uri) {
				if (!Paths.get(main + i).toFile().exists()) {
					Files.createDirectories(Paths.get(main + i));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createConfigurationFiles() {
		try {
			OusuConfiguration configuration = new OusuConfiguration();
			boolean settings = configuration.createSettingsFile(Paths.get("Settings.ini"));
			boolean sql = configuration.createSQLConfiguration(Paths.get("SQLConfiguration.ini"));
			if (settings || sql) {
				String[] configs = new String[] {(sql && settings)
						? "Settings.ini, SQLConfiguration.ini" : (sql) ? "SQLConfiguration.ini" : "Settings.ini"};
				CoreStarter.getLogger().info(String.format("Arquivo de configuração criado: %s", Arrays.toString(configs)));
				System.exit(0);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void addClassPathURL(File jar) throws Exception {
		Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
		method.setAccessible(true);
		method.invoke(Thread.currentThread().getContextClassLoader(), new Object[] { jar.toURI().toURL() });
	}

	public static <E> List<E> iteratorToList(Iterator<E> iterable){
		List<E> lista = new ArrayList<>();
		while (iterable.hasNext()) {
			E item = iterable.next();
			lista.add(item);
		}
		return lista;
	}

}
