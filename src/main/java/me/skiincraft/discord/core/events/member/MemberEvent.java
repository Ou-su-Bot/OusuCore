package me.skiincraft.discord.core.events.member;

import me.skiincraft.discord.core.event.Event;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public abstract class MemberEvent extends Event {
	
	public abstract Member getMember();
	public abstract Guild getGuild();

}
