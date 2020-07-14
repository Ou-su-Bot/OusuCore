package me.skiincraft.discord.core.adaptation;

import me.skiincraft.discord.core.plugin.Plugin;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public class GenericUser {
	
	private User user;
	private Member memberUser;
    private Plugin plugin;
	
    public GenericUser(Member member, Plugin plugin) {
    	this.user = member.getUser();
    	this.memberUser = member;
    	this.plugin = plugin;
    }
    
	public boolean isOwner() {
		return plugin.getDiscordInfo().getOwnerId() == user.getIdLong();
	}

	public String asMention() {
		return user.getAsMention();
	}
	
	public String getName() {
		return user.getName();
	}
	
	public String getNickname() {
		return memberUser.getNickname();
	}
	
	public String getCompleteName() {
		return user.getName() + user.getDiscriminator();
	}
	
	public long getId() {
		return user.getIdLong();
	}
	
	public PrivateChannel getPrivateChannel() {
		return user.openPrivateChannel().complete();
	}
	
	public boolean hasPermission(Permission... permission) {
		return memberUser.hasPermission(permission);
	}
}
