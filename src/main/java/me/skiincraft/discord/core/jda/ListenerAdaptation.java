package me.skiincraft.discord.core.jda;

import me.skiincraft.discord.core.configuration.GuildDB;
import me.skiincraft.discord.core.events.bot.BotJoinEvent;
import me.skiincraft.discord.core.events.bot.BotLeaveEvent;
import me.skiincraft.discord.core.events.bot.BotReactionEvent;
import me.skiincraft.discord.core.events.bot.BotReadyGuildEvent;
import me.skiincraft.discord.core.events.bot.BotReceivedMessage;
import me.skiincraft.discord.core.events.bot.BotUpdateAvatarEvent;
import me.skiincraft.discord.core.events.bot.BotUpdateNameEvent;
import me.skiincraft.discord.core.events.member.MemberJoinEvent;
import me.skiincraft.discord.core.events.member.MemberLeaveEvent;
import me.skiincraft.discord.core.events.member.MemberRoleEvent;
import me.skiincraft.discord.core.events.member.MemberUpdateNameEvent;
import me.skiincraft.discord.core.plugin.Plugin;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.self.SelfUpdateAvatarEvent;
import net.dv8tion.jda.api.events.self.SelfUpdateNameEvent;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerAdaptation extends ListenerAdapter {
	
	private Plugin plugin;
	
	public ListenerAdaptation(Plugin plugin) {
		this.plugin = plugin;
	}

	public void onUserUpdateName(UserUpdateNameEvent event) {
		//TODO Talvez adicione evento aqui.
	}

	public void onUserUpdateDiscriminator(UserUpdateDiscriminatorEvent event) {
		//TODO Talvez adicione evento aqui.
	}

	public void onUserUpdateAvatar(UserUpdateAvatarEvent event) {
		//TODO Talvez adicione evento aqui.
	}

	public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
		//TODO Talvez adicione evento aqui.
	}

	public void onUserUpdateActivityOrder(UserUpdateActivityOrderEvent event) {
		//TODO Talvez adicione evento aqui.
	}

	public void onUserTyping(UserTypingEvent event) {
		//TODO Talvez adicione evento aqui.
	}

	public void onUserActivityStart(UserActivityStartEvent event) {
		//TODO Talvez adicione evento aqui.
	}

	public void onUserActivityEnd(UserActivityEndEvent event) {
		//TODO Talvez adicione evento aqui.
	}

	public void onSelfUpdateAvatar(SelfUpdateAvatarEvent event) {
		plugin.getEventManager().callEvent(new BotUpdateAvatarEvent(event));
	}

	public void onSelfUpdateName(SelfUpdateNameEvent event) {
		plugin.getEventManager().callEvent(new BotUpdateNameEvent(event));
	}

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.isWebhookMessage()) return;
		if (event.getAuthor().isBot()) return;
		if (event.getAuthor().isFake()) return;
		if (event.getMessage().getContentRaw().split(" ")[0].length() < 3) return;
		
		GuildDB guildDB = new GuildDB(event.getGuild());
		String prefix = guildDB.get("prefix").toLowerCase();
		String[] args = event.getMessage().getContentRaw().split(" ");
		
		if (!args[0].toLowerCase().startsWith(prefix)) return;
		
		plugin.getEventManager().callEvent(new BotReceivedMessage(event.getChannel(), event.getMessage(), prefix));
	}

	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if (!event.getReaction().isSelf()) return;
		plugin.getEventManager().callEvent(new BotReactionEvent(event));
	}

	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		if (!event.getReaction().isSelf()) return;
		plugin.getEventManager().callEvent(new BotReactionEvent(event));
	}

	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		if (event.getAuthor().isFake()) return;
		plugin.getEventManager().callEvent(new BotReceivedMessage(event.getChannel(), event.getMessage(), "ou!"));
	}

	public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
		//TODO Evento para isso
	}

	public void onPrivateMessageReactionRemove(PrivateMessageReactionRemoveEvent event) {
		//TODO Evento para isso
	}

	public void onGuildReady(GuildReadyEvent event) {
		GuildDB db = new GuildDB(event.getGuild());
		db.create();
		plugin.getEventManager().callEvent(new BotReadyGuildEvent(event));
	}

	public void onGuildJoin(GuildJoinEvent event) {
		GuildDB db = new GuildDB(event.getGuild());
		db.create();
		plugin.getEventManager().callEvent(new BotJoinEvent(event));
	}

	public void onGuildLeave(GuildLeaveEvent event) {
		GuildDB db = new GuildDB(event.getGuild());
		db.delete();
		plugin.getEventManager().callEvent(new BotLeaveEvent(event));
	}
	
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		plugin.getEventManager().callEvent(new MemberJoinEvent(event));
	}

	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		plugin.getEventManager().callEvent(new MemberLeaveEvent(event));
	}

	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		plugin.getEventManager().callEvent(new MemberRoleEvent(event));
	}

	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		plugin.getEventManager().callEvent(new MemberRoleEvent(event));
	}

	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
		plugin.getEventManager().callEvent(new MemberUpdateNameEvent(event));
	}
}
