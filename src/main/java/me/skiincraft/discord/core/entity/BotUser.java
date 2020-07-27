package me.skiincraft.discord.core.entity;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

public interface BotUser {
	
	String getName();
	String getNickname();
	String getCompleteName();
	String getDiscriminator();
	String asMention();
	
	Guild getGuild();
	User getOriginalUser();
	Member getOriginalMember();
	
	long getId();
	boolean hasPermission(Permission... permissions);
	boolean isBotOwner();
	RestAction<PrivateChannel> openPrivateChannel();
	
	
	
	
	

}
