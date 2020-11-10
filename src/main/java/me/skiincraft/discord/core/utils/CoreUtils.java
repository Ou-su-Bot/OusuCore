package me.skiincraft.discord.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import me.skiincraft.discord.core.CoreStarter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

	public static Gson newGsonPretty(){
		return new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	}

	public static void createConfig() {
		File file = new File("settings.json");
		try {
			if (!file.exists()) {
				InputStreamReader input = new InputStreamReader(CoreStarter.class.getResourceAsStream("/config_settings.json"));
				FileWriter writer = new FileWriter(file);
				writer.write(newGsonPretty().toJson(new JsonParser().parse(input)));
				writer.close();
				CoreStarter.getLogger().warn("Configure o arquivo: settings.json");
				System.exit(0);
			}
		} catch (IOException e) {
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
