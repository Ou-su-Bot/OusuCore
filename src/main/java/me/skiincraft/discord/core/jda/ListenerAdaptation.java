package me.skiincraft.discord.core.jda;

import me.skiincraft.discord.core.configuration.GuildDB;
import me.skiincraft.discord.core.event.EventManager;
import me.skiincraft.discord.core.events.bot.BotJoinEvent;
import me.skiincraft.discord.core.events.bot.BotLeaveEvent;
import me.skiincraft.discord.core.events.bot.BotReactionEvent;
import me.skiincraft.discord.core.events.bot.BotReadyGuildEvent;
import me.skiincraft.discord.core.events.bot.BotReceivedMessage;
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
	private EventManager eventManager;
	
	public ListenerAdaptation(Plugin plugin) {
		this.plugin = plugin;
		this.eventManager = plugin.getEventManager();
	}

	public void onUserUpdateName(UserUpdateNameEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onUserUpdateName(event);
		}
	}

	public void onUserUpdateDiscriminator(UserUpdateDiscriminatorEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onUserUpdateDiscriminator(event);
		}
	}

	public void onUserUpdateAvatar(UserUpdateAvatarEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onUserUpdateAvatar(event);
		}
	}

	public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onUserUpdateOnlineStatus(event);
		}
	}

	public void onUserUpdateActivityOrder(UserUpdateActivityOrderEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onUserUpdateActivityOrder(event);
		}
	}

	public void onUserTyping(UserTypingEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onUserTyping(event);
		}
	}

	public void onUserActivityStart(UserActivityStartEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onUserActivityStart(event);
		}
	}

	public void onUserActivityEnd(UserActivityEndEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onUserActivityEnd(event);
		}
	}

	public void onSelfUpdateAvatar(SelfUpdateAvatarEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onSelfUpdateAvatar(event);
		}
	}

	public void onSelfUpdateName(SelfUpdateNameEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onSelfUpdateName(event);
		}
		plugin.getEventManager().callEvent(new BotUpdateNameEvent(event));
	}

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onGuildMessageReceived(event);
		}
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
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onGuildMessageReactionAdd(event);
		}
		if (!event.getReaction().isSelf()) return;
		plugin.getEventManager().callEvent(new BotReactionEvent(event));
	}

	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onGuildMessageReactionRemove(event);
		}
		if (!event.getReaction().isSelf()) return;
		plugin.getEventManager().callEvent(new BotReactionEvent(event));
	}

	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onPrivateMessageReceived(event);
		}
		if (event.getAuthor().isBot()) return;
		if (event.getAuthor().isFake()) return;
		plugin.getEventManager().callEvent(new BotReceivedMessage(event.getChannel(), event.getMessage(), "ou!"));
	}

	public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onPrivateMessageReactionAdd(event);
		}
	}

	public void onPrivateMessageReactionRemove(PrivateMessageReactionRemoveEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onPrivateMessageReactionRemove(event);
		}
	}

	public void onGuildReady(GuildReadyEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onGuildReady(event);
		}
		GuildDB db = new GuildDB(event.getGuild());
		db.create();
		plugin.getEventManager().callEvent(new BotReadyGuildEvent(event));
	}

	public void onGuildJoin(GuildJoinEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onGuildJoin(event);
		}
		GuildDB db = new GuildDB(event.getGuild());
		db.create();
		plugin.getEventManager().callEvent(new BotJoinEvent(event));
	}

	public void onGuildLeave(GuildLeaveEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onGuildLeave(event);
		}
		GuildDB db = new GuildDB(event.getGuild());
		db.delete();
		plugin.getEventManager().callEvent(new BotLeaveEvent(event));
	}
	
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onGuildMemberJoin(event);
		}
		plugin.getEventManager().callEvent(new MemberJoinEvent(event));
	}

	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onGuildMemberLeave(event);
		}
		plugin.getEventManager().callEvent(new MemberLeaveEvent(event));
	}

	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onGuildMemberRoleAdd(event);
		}
		plugin.getEventManager().callEvent(new MemberRoleEvent(event));
	}

	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onGuildMemberRoleRemove(event);
		}
		plugin.getEventManager().callEvent(new MemberRoleEvent(event));
	}

	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
		for (ListenerAdapter adapter : eventManager.getListenerAdapters()) {
			adapter.onGuildMemberUpdateNickname(event);
		}
		plugin.getEventManager().callEvent(new MemberUpdateNameEvent(event));
	}
}
