package me.skiincraft.discord.core.events.bot;

import me.skiincraft.discord.core.command.InteractChannel;

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
	
	private final Guild guild;
	private TextChannel textChannel;
	private PrivateChannel privChannel;
	private final String prefixUsed;
	private final Message message;
	private final InteractChannel interact;
	
	
	public BotReceivedMessage(TextChannel channel, Message message, String prefixUsed) {
		this.prefixUsed = prefixUsed;
		this.interact = new InteractChannel(channel);
		
		this.textChannel = channel;
		this.message = message;
		this.guild = channel.getGuild();
	}
	
	public BotReceivedMessage(PrivateChannel channel, Message message, String prefixUsed) {
		this.prefixUsed = prefixUsed;
		this.interact = null; //Channels.toChannelInteract(channel);
		this.privChannel = channel;
		this.message = message;
		this.guild = null;
	}
	
	public PrivateChannel getPrivChannel() {
		return privChannel;
	}
	
	public TextChannel getTextChannel() {
		return textChannel;
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public InteractChannel getInteract() {
		return interact;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public String getPrefixUsed() {
		return prefixUsed;
	}
	
	public boolean isPrivateMessage() {
		return textChannel == null;
	}

	public SelfUser getSelfUser() {
		return getTextChannel().getJDA().getSelfUser();
	}
	

}
