package me.skiincraft.discord.core.events.bot;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

public class BotJoinEvent extends BotEvent{
	
	private Guild guild; 
	private SelfUser selfUser;
	private Message message;
	
	public BotJoinEvent(GuildJoinEvent e) {
		this.guild = e.getGuild();
		this.selfUser = e.getJDA().getSelfUser();
	}
	
	public Message getJoinMessage() {
		return message;
	}
	
	public void setJoinMessage(MessageBuilder message){
		this.message = message.build();
	}

	public Guild getGuild() {
		return guild;
	}
	
	public SelfUser getSelfUser() {
		return selfUser;
	}
	
	

}
