package me.skiincraft.discord.core.events.bot;

import me.skiincraft.discord.core.entity.BotPrivChannel;
import me.skiincraft.discord.core.entity.BotTextChannel;
import me.skiincraft.discord.core.utils.Channels;
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
	private BotTextChannel botTextChannel;
	private Guild guild;
	private SelfUser selfUser;
	
	public BotDeleteMessage(Message deletedMessage, BotTextChannel textChannel, Guild guild) {
		this.deletedMessage = deletedMessage;
		this.messageId = deletedMessage.getIdLong();
		this.botTextChannel = textChannel;
		this.guild = guild;
		this.selfUser = guild.getJDA().getSelfUser();
	}
	
	public BotDeleteMessage(Message deletedMessage, TextChannel textChannel, Guild guild) {
		this.deletedMessage = deletedMessage;
		this.messageId = deletedMessage.getIdLong();
		this.botTextChannel = Channels.toBotChannel(textChannel);
		this.guild = guild;
		this.selfUser = guild.getJDA().getSelfUser();
	}
	
	public BotTextChannel getBotTextChannel() {
		return botTextChannel;
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
