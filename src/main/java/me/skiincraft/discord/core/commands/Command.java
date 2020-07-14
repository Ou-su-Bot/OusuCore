package me.skiincraft.discord.core.commands;

import java.util.List;

import me.skiincraft.discord.core.adaptation.GenericUser;
import me.skiincraft.discord.core.database.GuildDB;
import me.skiincraft.discord.core.multilanguage.LanguageManager;
import me.skiincraft.discord.core.multilanguage.LanguageManager.Language;
import me.skiincraft.discord.core.plugin.Plugin;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class Command extends ChannelContext {

	private String name;
	private List<String> aliases;
	private String usage;
	
	private Plugin plugin;
	
	private TextChannel channel;
	
	public Command(String name, List<String> aliases, String usage) {
		this.name = name;
		this.aliases = aliases;
		this.usage = usage;
	}
	
	public String getCommandDescription(LanguageManager language) {
		return language.getString("CommandDescription", name.toUpperCase() +"_DESCRIPTION");
	}
	
	public abstract void execute(GenericUser user, String[] args, TextChannel channel);

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
		return plugin;
	}
	
	public TextChannel getTextChannel() {
		return channel;
	}
	
	public LanguageManager getLanguageManager() {
		GuildDB db = new GuildDB(plugin, getTextChannel().getGuild());
		return new LanguageManager(plugin, Language.valueOf(db.get("language")));
	}
	
}
