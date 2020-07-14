package me.skiincraft.discord.core.events.bot;

import me.skiincraft.discord.core.event.Event;
import net.dv8tion.jda.api.entities.SelfUser;

public abstract class BotEvent extends Event {
	
	public abstract SelfUser getSelfUser();

}
