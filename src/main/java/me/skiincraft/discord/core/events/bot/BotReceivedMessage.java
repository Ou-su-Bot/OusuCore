package me.skiincraft.discord.core.events.bot;

import me.skiincraft.discord.core.entity.BotPrivChannel;
import me.skiincraft.discord.core.entity.BotTextChannel;
import me.skiincraft.discord.core.entity.ChannelInteract;
import me.skiincraft.discord.core.utils.Channels;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Evento ao receber uma mensagem direta (usando prefix).
 * <br>Este evento será chamando ao receber uma mensagem (comando?)
 * 
 * Pode ser utilizado para criar comandos em formas de evento.
 */
public class BotReceivedMessage extends BotEvent {
	
	private Guild guild;
	private BotTextChannel botTextChannel;
	private BotPrivChannel botPrivChannel;
	private String prefixUsed;
	private Message message;
	private ChannelInteract interact;
	
	
	public BotReceivedMessage(TextChannel channel, Message message, String prefixUsed) {
		this.prefixUsed = prefixUsed;
		this.interact = Channels.toChannelInteract(channel);
		this.botTextChannel = Channels.toBotChannel(channel);
		this.message = message;
		this.guild = channel.getGuild();
	}
	
	public BotReceivedMessage(PrivateChannel channel, Message message, String prefixUsed) {
		this.prefixUsed = prefixUsed;
		this.interact = null; //Channels.toChannelInteract(channel);
		this.botPrivChannel = Channels.toBotChannel(channel);
		this.message = message;
		this.guild = null;
	}
	
	public BotPrivChannel getBotPrivChannel() {
		return botPrivChannel;
	}
	
	public BotTextChannel getBotTextChannel() {
		return botTextChannel;
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public ChannelInteract getInteract() {
		return interact;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public String getPrefixUsed() {
		return prefixUsed;
	}
	
	public boolean isPrivateMessage() {
		return botTextChannel == null;
	}

	public SelfUser getSelfUser() {
		return getBotTextChannel().getJda().getSelfUser();
	}
	

}
