package me.skiincraft.discord.core.events.bot;

import me.skiincraft.discord.core.entity.ChannelInteract;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Evento ao enviar uma mensagem com o bot {@link ChannelInteract}.
 * <br>Este evento será chamando ao enviar uma mensagem.
 * 
 * Pode ser utilizado para notificações.
 */
public class BotSendMessage extends BotEvent {
	
	private Guild guild;
	private TextChannel textChannel;
	private Message message;
	private SelfUser selfUser;
	
	public BotSendMessage(TextChannel channel, Message message, SelfUser self) {
		this.guild = channel.getGuild();
		this.textChannel = channel;
		this.message = message;
		this.selfUser = self;
	}
	
	public SelfUser getSelfUser() {
		return selfUser;
	}
	
	public TextChannel getTextChannel() {
		return textChannel;
	}
	
	public Guild getGuild() {
		return guild;
	}

	public Message getMessage() {
		return message;
	}
}
