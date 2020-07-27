package me.skiincraft.discord.core.events.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;

/**
 * Evento ao iniciar em uma {@link Guild}.
 * <br>Este evento será chamando ao bot conectar em algum servidor.
 * 
 * Pode ser utilizado para notificações. 
 * - <b>(não recomendado para enviar mensagens)</b>.
 */
public class BotReadyGuildEvent extends BotEvent {
	
	private Guild guild; 
	private SelfUser selfUser;
	
	public BotReadyGuildEvent(GuildReadyEvent e) {
		this.guild = e.getGuild();
		this.selfUser = e.getJDA().getSelfUser();
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public SelfUser getSelfUser() {
		return selfUser;
	}

}
