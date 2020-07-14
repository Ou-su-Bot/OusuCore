package me.skiincraft.discord.core.adaptation;

import me.skiincraft.discord.core.commands.ChannelContext;
import me.skiincraft.discord.core.database.GuildDB;
import me.skiincraft.discord.core.multilanguage.LanguageManager;
import me.skiincraft.discord.core.plugin.Plugin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class Chat extends ChannelContext {

	private TextChannel channel;
	private Plugin plugin;
	private LanguageManager language;
	
	private long callMessageId;
	
	public Chat(TextChannel textChannel, Plugin plugin) {
		this.channel = textChannel;
		this.plugin = plugin;
		this.language = new LanguageManager(plugin, textChannel.getGuild());
		
	}
	
	public LanguageManager getLanguageManager() {
		return language;
	}
	
	public JDA getJDA() {
		return channel.getJDA();
	}
	
	public Guild getGuild() {
		return channel.getGuild();
	}
	
	public TextChannel getTextChannel() {
		return channel;
	}
	
	public long getCallMessageId() {
		return callMessageId;
	}
	
	public void addReaction(Emote emote) {
		channel.addReactionById(callMessageId, emote).queue();
	}
	
	public void deleteCallMessage() {
		channel.deleteMessageById(callMessageId).queue();
	}
	
	public void sendUsage(String usage) {
		reply(new GuildDB(plugin, channel.getGuild()).get("prefix").concat(" ") + usage);
	}
	

	
}
