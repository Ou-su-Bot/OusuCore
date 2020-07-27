package me.skiincraft.discord.core.events.member;

import java.util.List;

import me.skiincraft.discord.core.events.enums.RoleEventType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;

public class MemberRoleEvent extends MemberEvent {

	private RoleEventType eventType;
	private Guild guild;
	private Member member;
	private List<Role> roles;
	
	public MemberRoleEvent(GuildMemberRoleAddEvent e) {
		eventType = RoleEventType.ADD;
		guild = e.getGuild();
		member = e.getMember();
	}
	
	public MemberRoleEvent(GuildMemberRoleRemoveEvent e) {
		eventType = RoleEventType.REMOVE;
		guild = e.getGuild();
		member = e.getMember();
	}
	
	public RoleEventType getEventType() {
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
