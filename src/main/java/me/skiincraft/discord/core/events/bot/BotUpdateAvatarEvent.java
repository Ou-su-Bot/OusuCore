package me.skiincraft.discord.core.events.bot;

import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.self.SelfUpdateAvatarEvent;

public class BotUpdateAvatarEvent extends BotEvent {

	private final SelfUser selfUser;
	private final String newAvatarUrl;
	private final String newAvatarId;
	
	public BotUpdateAvatarEvent(SelfUpdateAvatarEvent e) {
		selfUser = e.getSelfUser();
		newAvatarUrl = e.getNewAvatarUrl();
		newAvatarId = e.getNewAvatarUrl();
	}
	
	public SelfUser getSelfUser() {
		return selfUser;
	}
	
	public String getNewAvatarId() {
		return newAvatarId;
	}
	
	public String getNewAvatarUrl() {
		return newAvatarUrl;
	}

}
