package me.skiincraft.discord.core.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.commands.Command;
import me.skiincraft.discord.core.exception.ConfigurationNotFound;
import me.skiincraft.discord.core.objects.PluginConfig;
import me.skiincraft.discord.core.objects.PluginData;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public interface PluginManager {
	
	List<Plugin> getLoadedPlugins();
	Plugin getLoadedPlugin(String plugin);
	boolean existsLoadedPlugin(String plugin);
	PluginConfig getPluginConfig(String jarfile);
	PluginConfig getPluginConfig(String path, String jarfile) throws FileNotFoundException;
	PluginConfig getPluginConfig(File file);
	PluginData getPluginData(String botname, String bot_main, String filename) throws ConfigurationNotFound;
	Plugin[] getPlugins(Path path);
	Plugin getPluginByBotId(long id);
	Plugin getPluginByBotId(String id);
	void registerCommands(Command command, OusuPlugin plugin);
	void registerEvents(ListenerAdapter listener, OusuPlugin plugin);
	
	void restartPlugin(Plugin plugin);
	void disablePlugin(Plugin plugin);
	void enablePlugin(Plugin plugin);
	
	static PluginManager getPluginManager() {
		return OusuCore.pluginManager;
	}
}
