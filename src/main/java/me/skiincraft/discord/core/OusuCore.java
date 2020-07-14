package me.skiincraft.discord.core;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.plugin.PluginManager;
import me.skiincraft.discord.core.view.OusuViewer;

public class OusuCore {
	
	public static final int EXIT_FORCEEXIT = 0;
	public static final int EXIT_SECURE = 1;
	public static final int EXIT_RESTART = 2;
	public static final int EXIT_ERROR = 3;
	
	public static PluginManager pluginManager;
	public static List<ThreadGroup> threadgroups = new ArrayList<>();
	
	private static String[] jvmArguments;
	public static final String[] arguments = jvmArguments;
	
	public static void main(String[] args) throws IOException {
		jvmArguments = args;
		//start();
		//OusuViewer.start(args);
	}
	
	public static void start() {
		OusuViewer.start(jvmArguments, () -> {
			//pluginManager = new SimplePluginManager();
			//loadPlugins();
		});
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
		List<Plugin> allplugins = pluginManager.getLoadedPlugins();
		for (Plugin plugin : allplugins) {
			pluginManager.disablePlugin(plugin);
		}
		
		
	}
	
	private static void loadPlugins() {
		Plugin[] plugins = pluginManager.getPlugins(Paths.get("bots"));
		for (Plugin plugin : plugins) {
			pluginManager.enablePlugin(plugin);
		}
	}
	

}
