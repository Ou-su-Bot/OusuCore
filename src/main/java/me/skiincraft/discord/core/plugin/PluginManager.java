package me.skiincraft.discord.core.plugin;

public interface PluginManager {
	
	void startPlugin();
	void stopPlugin();
	Plugin getPlugin();
	boolean hasStarted();

}
