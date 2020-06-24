package me.skiincraft.discord.core.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.commands.Command;
import me.skiincraft.discord.core.exception.BotConfigNotFoundException;
import me.skiincraft.discord.core.objects.PluginConfig;
import me.skiincraft.discord.core.objects.PluginData;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public interface PluginManager {
	
	ArrayList<Plugin> getPlugins();
	Plugin getPlugin(String plugin);
	boolean hasPlugin(String plugin);
	PluginConfig getPluginConfig(String jarfile);
	PluginConfig getPluginConfig(String path, String jarfile) throws FileNotFoundException;
	PluginConfig getPluginConfig(File file);
	PluginData getPluginData(String botname, String bot_main, String filename) throws BotConfigNotFoundException;
	@Deprecated
	Plugin[] getPlugins(File path);
	Plugin getPluginByBotId(long id);
	Plugin getPluginByBotId(String id);
	void registerCommands(Command command, OusuPlugin plugin);
	void registerEvents(ListenerAdapter events, OusuPlugin plugin);
	
	void disablePlugin(Plugin plugin);
	void enablePlugin(Plugin plugin);
	
	static PluginManager getPluginManager() {
		return OusuCore.pluginManager;
	}
}
