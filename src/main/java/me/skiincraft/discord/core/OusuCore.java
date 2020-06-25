package me.skiincraft.discord.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.skiincraft.discord.core.exception.BotConfigNotFoundException;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.plugin.PluginManager;
import me.skiincraft.discord.core.plugin.SimplePluginManager;

public class OusuCore extends OusuPlugin{
	
	public static PluginManager pluginManager;
	public static List<ThreadGroup> threadgroups = new ArrayList<>();
	
	public static void main(String[] args) throws BotConfigNotFoundException, ClassNotFoundException {
		pluginManager = new SimplePluginManager();
		
		Plugin[] plugins = pluginManager.getPlugins(new File("bots\\"));
		for (Plugin plugin : plugins) {
			pluginManager.enablePlugin(plugin);
		}
	}
	

}
