package me.skiincraft.discord.core.plugin;

import java.nio.file.Path;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.configuration.Command;
import me.skiincraft.discord.core.event.EventManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public interface PluginManager {
	
	Plugin getPlugin();
	void loadPlugin(Path path);
	
	void registerCommands(Command command);
	void registerEvents(ListenerAdapter listener);
	
	void restartPlugin();
	void disablePlugin();
	void enablePlugin();
	
	default EventManager getEventManager() {
		return getPlugin().getEventManager();
	}
	
	static PluginManager getPluginManager() {
		return OusuCore.getPluginManager();
	}
}
