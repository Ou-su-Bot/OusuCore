package me.skiincraft.discord.core;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Collectors;

import me.skiincraft.discord.core.configuration.PropertieConfig;
import me.skiincraft.discord.core.impl.PluginManagerImpl;
import me.skiincraft.discord.core.plugin.PluginManager;
import me.skiincraft.discord.core.utils.CoreUtils;
import me.skiincraft.discord.core.utils.IntegerUtils;

public class OusuCore {
	
	public static final int EXIT_FORCEEXIT = 0;
	public static final int EXIT_SECURE = 1;
	public static final int EXIT_RESTART = 2;
	public static final int EXIT_ERROR = 3;
	public static String[] args;
	
	private static PluginManager pluginManager;
	
	public static void main(String[] args) throws IOException {
		/* 
		 * TODO Criando pastas.
		 */
		CoreUtils.createPath("bots", "library", "dependency", "logs");
		
		/* 
		 * TODO Criando arquivo de configuração
		 */
		PropertieConfig config = new PropertieConfig();
		Properties properties = config.getProperties();
		
		/*
		 * TODO Preparando plugin manager (bot manager)
		 */
		Runnable runnable = new Runnable() {
			
			public void run() {
				OusuCore.args = args;
				pluginManager = new PluginManagerImpl();
				pluginManager.startPlugin();
				String restartValue = (properties.containsKey("auto-restart")) ? properties.getProperty("auto-restart")
						: null;

				if (restartValue == null || !isBoolean(restartValue)) {
					System.out.println(properties.keySet().stream().collect(Collectors.toList()));
					System.out.println("Há opções invalidas no config.properties");
					return;
				}

				if (Boolean.valueOf(restartValue) == true) {
					new Thread(() -> {
						long restarttime = (properties.containsKey("restart-time"))
								? (IntegerUtils.isNumeric(properties.getProperty("restart-time")))
										? Long.valueOf(properties.getProperty("restart-time"))
										: 60L
								: 60L;
						try {
							System.out.println("Programado para reiniciar");
							Thread.sleep(restarttime * (60 * 1000L));
							restart(args);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}).start();
				}
			}
		};
		
		/*
		 * TODO Loading Library
		 */
		boolean isJda = false;
		try {
			isJda = loadLibrary();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (!isJda) {
			throw new IOException("Não foi possivel carregas as bibiliotecas, JDA não localizado.");
		}
		
		/*
		 * Loading Dependencies
		 */
		try {
			loadDependency();
		} catch (Exception e) {
			e.printStackTrace();
		}
		runnable.run();
	}
	
	private static boolean loadLibrary() throws Exception {
		for (Path path : Files.newDirectoryStream(Paths.get("library"))) {
			try {
				if (path.toFile().getName().contains(".jar")) {
					URLClassLoader c = new URLClassLoader(new URL[] { path.toFile().toURI().toURL() });
					if (c.findResource("net/dv8tion/jda") != null) {
						CoreUtils.addClassPathURL(path.toFile());
						System.out.println("Library: JDA loaded.");
						c.close();return true;
						
					}
					c.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private static void loadDependency() throws Exception {
		for (Path path : Files.newDirectoryStream(Paths.get("dependency"))) {
			if (path.toFile().getName().contains(".jar")) {
				CoreUtils.addClassPathURL(path.toFile());
			}
		}
	}
	
	public static void restart() throws IOException {
		restart(args);
	}
	
	public static void restart(String[] args) throws IOException {
		StringBuffer command = new StringBuffer();
		command.append(System.getProperty("java.home")
				.concat(File.separator)
				.concat("bin")
				.concat(File.separator)
				.concat("java "));
		
		for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
			command.append(jvmArg + " ");
		}
		
		command.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
		command.append(OusuCore.class.getName()).append(" ");
		if (args != null) {
			for (String arg : args) {
				command.append(arg).append(" ");
			}
		}
		Runtime.getRuntime().exec(command.toString());
		System.exit(EXIT_RESTART);
	}

	public static void exit(int exitcode) {
		//pluginManager.disablePlugin();
	}
	
	public static PluginManager getPluginManager() {
		return pluginManager;
	}
	
	
	private static boolean isBoolean(String string) {
		if (string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false")) {
			return true;
		}
		return false;
	}
}
