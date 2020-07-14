package me.skiincraft.discord.core.view.console;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import me.skiincraft.discord.core.plugin.Plugin;

public abstract class ConsoleCommand {
	
	private String name;
	private List<String> aliases;
	private String description;
	
	private boolean pluginInteraction;
	
	public ConsoleCommand(@NotNull String name, List<String> aliases, String description, boolean hasPluginInteraction) {
		this.name = name;
		this.aliases = aliases;
		this.description = description;
		this.pluginInteraction = hasPluginInteraction;
	}
	
	public abstract void execute(String[] args, Plugin interacted);

	public boolean hasPluginInteraction() {
		return pluginInteraction;
	}
	
	public String getName() {
		return name;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public String getDescription() {
		return description;
	}
}
