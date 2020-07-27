package me.skiincraft.discord.core;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.MethodUtils;

import me.skiincraft.discord.core.event.EventManager;
import me.skiincraft.discord.core.file.ConfigurationProperties;
import me.skiincraft.discord.core.impl.PluginManagerImpl;
import me.skiincraft.discord.core.plugin.PluginInit;
import me.skiincraft.discord.core.plugin.PluginManager;
import me.skiincraft.discord.core.view.OusuViewer;

public class OusuCore {
	
	public static final int EXIT_FORCEEXIT = 0;
	public static final int EXIT_SECURE = 1;
	public static final int EXIT_RESTART = 2;
	public static final int EXIT_ERROR = 3;
	
	private static PluginManager pluginManager;
	private static OusuViewer ousuViewer;
	private static String[] jvmArguments;
	
	static void createPath(String... uri) throws IOException {
		for (String i : uri) {
			if (!Paths.get(i).toFile().exists()) {
				Files.createDirectories(Paths.get(i));
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		/* 
		 * Creating paths.
		 */
		createPath("bots", "dependency", "logs");
		
		/* 
		 * Creating "configuration files"
		 */
		ConfigurationProperties config = new ConfigurationProperties();
		Properties properties = config.getProperties();
		
		jvmArguments = args;
		/* 
		 * Creating task to run on the JavaFX Thread
		 */
		Runnable runnable = () -> {
			pluginManager = new PluginManagerImpl(new PluginInit());
			pluginManager.loadPlugin(Paths.get("bots"));
			pluginManager.enablePlugin();
		};
		ousuViewer = new OusuViewer(runnable, jvmArguments);
		/* 
		 * Checking if the interface is active in the configurations
		 */
		String interfaceValue = (properties.containsKey("interface")) ? properties.getProperty("interface")
				: null;
		if (interfaceValue == null || !isBoolean(interfaceValue)) {
			System.out.println(properties.keySet().stream().collect(Collectors.toList()));
			System.out.println("Há opções invalidas no config.properties");
			return;
		}
		
		/* 
		 * Running JavaFX application
		 */
		if (Boolean.valueOf(interfaceValue) == true) {
			start(runnable);
		} else {
			runnable.run();
		}
	}
	
	private static void start(Runnable runnable) {
		try {
			MethodUtils.invokeMethod(ousuViewer, true, "launchViewer");
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
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
	
	/**
	 * OusuCore.
	 * @param exitcode is {@link OusuCore} exitcode
	 *
	 */
	public static void exit(int exitcode) {
		pluginManager.disablePlugin();
	}
	
	private static boolean isBoolean(String string) {
		if (string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false")) {
			return true;
		}
		return false;
	}
	
	public static OusuViewer getOusuViewer() {
		return ousuViewer;
	}

	public static EventManager getEventManager() {
		return getPluginManager().getEventManager();
	}
	
	public static PluginManager getPluginManager() {
		return pluginManager;
	}
	
}
