package me.skiincraft.discord.core.events.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;

/**
 * Evento ao entrar em uma {@link Guild}.
 * <br>Este evento será chamando ao entrar em algum servidor.
 * 
 * Pode ser utilizado para enviar alguma notificação.
 */
public class BotLeaveEvent extends BotEvent {

	private Guild guild; 
	private SelfUser selfUser;
	
	public BotLeaveEvent(GuildLeaveEvent e) {
		this.guild = e.getGuild();
		this.selfUser = e.getJDA().getSelfUser();
	}
	
	public BotLeaveEvent(Guild guild) {
		this.guild = guild;
		this.selfUser = guild.getJDA().getSelfUser();
	}
	
	public Guild getGenericGuild() {
		return guild;
	}
	
	public SelfUser getSelfUser() {
		return selfUser;
	}

}
