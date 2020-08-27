package me.skiincraft.discord.core.configuration;

import java.util.List;

import me.skiincraft.discord.core.entity.ChannelInteract;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.plugin.PluginManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public abstract class Command extends ChannelInteract {

	private String name;
	private List<String> aliases;
	private String usage;
	
	private TextChannel channel;
	
	public Command(String name, List<String> aliases, String usage) {
		this.name = name;
		this.aliases = aliases;
		this.usage = usage;
	}
	
	public String getCommandDescription(LanguageManager language) {
		return language.getString("CommandDescription", name.toUpperCase() +"_DESCRIPTION");
	}
	
	public abstract void execute(User user, String[] args, TextChannel channel);

	public String getCommandName() {
		return name;
	}


	public List<String> getAliases() {
		return aliases;
	}

	public String getUsage() {
		return usage;
	}
	
	public Plugin getPlugin() {
		return PluginManager.getPluginManager().getPlugin();
	}
	
	public TextChannel getTextChannel() {
		return channel;
	}
	
	public LanguageManager getLanguageManager() {
		return new LanguageManager(getTextChannel().getGuild());
	}
	
}
