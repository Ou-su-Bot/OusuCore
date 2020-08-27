package me.skiincraft.discord.core.events.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Evento ao deletar uma mensagem com o bot
 * <br>Este evento será chamando ao deletar uma mensagem
 * utilizando o {@link BotTextChannel} ou {@link BotPrivChannel}.
 */
public class BotDeleteMessage extends BotEvent {
	
	private Message deletedMessage;
	private long messageId;
	private TextChannel textChannel;
	private Guild guild;
	private SelfUser selfUser;
	
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
