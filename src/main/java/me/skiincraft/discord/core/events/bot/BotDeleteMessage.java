package me.skiincraft.discord.core.events.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

public class BotDeleteMessage extends BotEvent {
	
	private final Message deletedMessage;
	private final long messageId;
	private final TextChannel textChannel;
	private final Guild guild;
	private final SelfUser selfUser;
	
	public BotDeleteMessage(Message deletedMessage, TextChannel textChannel, Guild guild) {
		this.deletedMessage = deletedMessage;
		this.messageId = deletedMessage.getIdLong();
		this.textChannel = textChannel;
		this.guild = guild;
		this.selfUser = guild.getJDA().getSelfUser();
	}
	
	
	public TextChannel getBotTextChannel() {
		return textChannel;
	}
	
	public Message getDeletedMessage() {
		return deletedMessage;
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public long getMessageId() {
		return messageId;
	}
	
	public Member getSelfMember() {
		return guild.getSelfMember();
	}
	
	public SelfUser getSelfUser() {
		return selfUser;
	}
	
	

}
