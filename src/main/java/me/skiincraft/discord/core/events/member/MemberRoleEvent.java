package me.skiincraft.discord.core.events.member;

import java.util.List;

import me.skiincraft.discord.core.events.enums.EventType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;

public class MemberRoleEvent extends MemberEvent {

	private final EventType eventType;
	private final Guild guild;
	private final Member member;
	private List<Role> roles;
	
	public MemberRoleEvent(GuildMemberRoleAddEvent e) {
		eventType = EventType.ADD;
		guild = e.getGuild();
		member = e.getMember();
	}
	
	public MemberRoleEvent(GuildMemberRoleRemoveEvent e) {
		eventType = EventType.REMOVE;
		guild = e.getGuild();
		member = e.getMember();
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public Member getMember() {
		return member;
	}
}
