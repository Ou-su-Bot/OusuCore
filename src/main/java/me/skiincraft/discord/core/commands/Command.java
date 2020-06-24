package me.skiincraft.discord.core.commands;

import java.util.List;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public abstract class Command {

	private ChannelContext context;
	private String name;
	private List<String> aliases;
	private String usage;
	
	public Command(String name, List<String> aliases, String usage) {
		this.name = name;
		this.aliases = aliases;
		this.usage = usage;
	}
	
	public final String getCommandDescription(String name) {
		return name;
	}
	
	public ChannelContext response() {
		return context;
	}

	public abstract void execute(User user, String[] args, TextChannel channel);

	public ChannelContext getContext() {
		return context;
	}

	public String getCommandName() {
		return name;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public String getUsage() {
		return usage;
	}
	
}
