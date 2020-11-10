package me.skiincraft.discord.core.events.member;

import me.skiincraft.discord.core.event.Updateable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;

public class MemberUpdateNameEvent extends MemberEvent implements Updateable<String> {
	
	private final Member member;
	private final String newName;
	private final String oldName;
	private Guild guild;
	
	public MemberUpdateNameEvent(GuildMemberUpdateNicknameEvent e) {
		this.member = e.getMember();
		this.newName = e.getNewNickname();
		this.oldName = e.getOldNickname();
	}

	public Member getMember() {
		return member;
	}

	public Guild getGuild() {
		return guild;
	}

	public String getUpdate() {
		return newName;
	}
	
	public String getOutDated() {
		return oldName;
	}
}
