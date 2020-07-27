package me.skiincraft.discord.core.events.bot;

import me.skiincraft.discord.core.entity.BotTextChannel;
import me.skiincraft.discord.core.entity.ChannelInteract;
import me.skiincraft.discord.core.utils.Channels;
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
	private BotTextChannel botTextChannel;
	private Message message;
	private SelfUser selfUser;
	
	public BotSendMessage(TextChannel channel, Message message, SelfUser self) {
		this.guild = channel.getGuild();
		this.botTextChannel = Channels.toBotChannel(channel);
		this.message = message;
		this.selfUser = self;
	}
	
	public SelfUser getSelfUser() {
		return selfUser;
	}
	
	public BotTextChannel getBotTextChannel() {
		return botTextChannel;
	}
	
	public Guild getGuild() {
		return guild;
	}

	public Message getMessage() {
		return message;
	}
}
