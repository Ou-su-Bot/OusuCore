package me.skiincraft.discord.core.command;

import me.skiincraft.discord.core.language.Language;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public abstract class Command {

	private final String name;
	private final List<String> aliases;
	private final String usage;
	
	public Command(String name, List<String> aliases, String usage) {
		this.name = name;
		this.aliases = aliases;
		this.usage = usage;
	}
	
	public String getCommandDescription(Language language) {
		return language.getString("command", "description", name.toLowerCase());
	}

	public abstract void execute(Member user, String[] args, InteractChannel channel);

	public String getCommandName() {
		return name;
	}


	public List<String> getAliases() {
		return aliases;
	}

	public String getUsage() {
		return usage;
	}
	
	public Language getLanguage(Guild guild) {
		return Language.getGuildLanguage(guild);
	}
	
}
