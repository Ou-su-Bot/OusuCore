package me.skiincraft.discord.core.utils;

import me.skiincraft.discord.core.entity.BotPrivChannel;
import me.skiincraft.discord.core.entity.BotTextChannel;
import me.skiincraft.discord.core.entity.BotUser;
import me.skiincraft.discord.core.entity.ChannelInteract;
import me.skiincraft.discord.core.impl.BotPrivChannelImpl;
import me.skiincraft.discord.core.impl.BotTextChannelImpl;
import me.skiincraft.discord.core.impl.BotUserImpl;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class Channels {
	
	public static ChannelInteract toChannelInteract(TextChannel textChannel) {
		return new ChannelInteract() {
			
			protected MessageChannel getTextChannel() {
				return textChannel;
			}
		};
	}
	
	public static BotTextChannel toBotChannel(TextChannel textChannel) {
		return new BotTextChannelImpl(textChannel);
	}
	
	public static BotPrivChannel toBotChannel(PrivateChannel textChannel) {
		return new BotPrivChannelImpl(textChannel);
	}
	
	public static BotUser toBotUser(Member member) {
		return new BotUserImpl(member);
	}
	
	public static BotUser toBotUser(User user) {
		return new BotUserImpl(user);
	}
	

}
