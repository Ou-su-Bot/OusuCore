package me.skiincraft.discord.core.events.bot;

import java.util.List;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

/**
 * Evento ao entrar em uma {@link Guild}.
 * <br>Este evento será chamando ao entrar em algum servidor.
 * 
 * Pode ser utilizado para enviar alguma mensagem de boas vindas.
 */
public class BotJoinEvent extends BotEvent{
	
	private Guild guild; 
	private SelfUser selfUser;
	private Message message;
	
	public BotJoinEvent(GuildJoinEvent e) {
		this.guild = e.getGuild();
		this.selfUser = e.getJDA().getSelfUser();
	}
	
	public BotJoinEvent(Guild guild) {
		this.guild = guild;
		this.selfUser = guild.getJDA().getSelfUser();
	}
	
	public Message getJoinMessage() {
		return message;
	}
	
	public void setJoinMessage(MessageBuilder message){
		this.message = message.build();
	}
	
	public void sendJoinMessage() {
		List<TextChannel> channels = guild.getTextChannelCache().asList();
		for (TextChannel channel:channels) {
			if (channel.canTalk()) {
				channel.sendMessage(getJoinMessage());
				break;
			}
		}
	}

	public Guild getGuild() {
		return guild;
	}
	
	public SelfUser getSelfUser() {
		return selfUser;
	}
}
