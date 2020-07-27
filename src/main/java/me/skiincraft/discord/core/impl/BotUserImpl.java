package me.skiincraft.discord.core.impl;

import me.skiincraft.discord.core.entity.BotUser;
import me.skiincraft.discord.core.plugin.PluginManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

public class BotUserImpl implements BotUser {

	private Member member;
	private User user;
	
	public BotUserImpl(Member member) {
		this.member = member;
	}
	
	public BotUserImpl(User user) {
		this.user = user;
	}
	
	public String getName() {
		return (member == null) ? user.getName() : member.getEffectiveName();
	}

	public String getNickname() {
		return (member == null) ? user.getName() : member.getNickname();
	}

	public String getCompleteName() {
		return (member == null) ? user.getName() + "#" + user.getDiscriminator()
				: member.getEffectiveName() + member.getUser().getDiscriminator();
	}

	public String getDiscriminator() {
		return (member == null) ? user.getDiscriminator() : member.getUser().getDiscriminator();
	}

	public String asMention() {
		return (member == null) ? user.getAsMention() : member.getAsMention();
	}

	public Guild getGuild() {
		return (member == null) ? null : member.getGuild();
	}

	public User getOriginalUser() {
		return (user == null) ? member.getUser() : user;
	}

	public Member getOriginalMember() {
		return (member == null) ? (Member) user : member;
	}

	public long getId() {
		return (member == null) ? user.getIdLong() : member.getIdLong();
	}

	public boolean hasPermission(Permission... permissions) {
		return member.hasPermission(permissions);
	}

	public boolean isBotOwner() {
		return getId() == PluginManager.getPluginManager().getPlugin().getDiscordInfo().getOwnerId();
	}

	public RestAction<PrivateChannel> openPrivateChannel() {
		return (member == null) ? user.openPrivateChannel() : member.getUser().openPrivateChannel();
	}

}
