package me.skiincraft.discord.core.jda;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.configuration.GuildDB;
import me.skiincraft.discord.core.events.bot.*;
import me.skiincraft.discord.core.events.member.MemberJoinEvent;
import me.skiincraft.discord.core.events.member.MemberLeaveEvent;
import me.skiincraft.discord.core.events.member.MemberRoleEvent;
import me.skiincraft.discord.core.events.member.MemberUpdateNameEvent;
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
import net.dv8tion.jda.api.events.user.update.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ListenerAdaptation extends ListenerAdapter {

    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onUserUpdateName(event);
        }
    }

    public void onUserUpdateDiscriminator(@NotNull UserUpdateDiscriminatorEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onUserUpdateDiscriminator(event);
        }
    }

    public void onUserUpdateAvatar(@NotNull UserUpdateAvatarEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onUserUpdateAvatar(event);
        }
    }

    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onUserUpdateOnlineStatus(event);
        }
    }

    public void onUserUpdateActivityOrder(@NotNull UserUpdateActivityOrderEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onUserUpdateActivityOrder(event);
        }
    }

    public void onUserTyping(@NotNull UserTypingEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onUserTyping(event);
        }
    }

    public void onUserActivityStart(@NotNull UserActivityStartEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onUserActivityStart(event);
        }
    }

    public void onUserActivityEnd(@NotNull UserActivityEndEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onUserActivityEnd(event);
        }
    }

    public void onSelfUpdateAvatar(@NotNull SelfUpdateAvatarEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onSelfUpdateAvatar(event);
        }
    }

    public void onSelfUpdateName(@NotNull SelfUpdateNameEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onSelfUpdateName(event);
        }
        OusuCore.getEventManager().callEvent(new BotUpdateNameEvent(event));
    }

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
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

        OusuCore.getEventManager().callEvent(new BotReceivedMessage(event.getChannel(), event.getMessage(), prefix));
    }

    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onGuildMessageReactionAdd(event);
        }
        if (!event.getReaction().isSelf()) return;
        OusuCore.getEventManager().callEvent(new BotReactionEvent(event));
    }

    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onGuildMessageReactionRemove(event);
        }
        if (!event.getReaction().isSelf()) return;
        OusuCore.getEventManager().callEvent(new BotReactionEvent(event));
    }

    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onPrivateMessageReceived(event);
        }
        if (event.getAuthor().isBot()) return;
        if (event.getAuthor().isFake()) return;
        OusuCore.getEventManager().callEvent(new BotReceivedMessage(event.getChannel(), event.getMessage(), "ou!"));
    }

    public void onPrivateMessageReactionAdd(@NotNull PrivateMessageReactionAddEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onPrivateMessageReactionAdd(event);
        }
    }

    public void onPrivateMessageReactionRemove(@NotNull PrivateMessageReactionRemoveEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onPrivateMessageReactionRemove(event);
        }
    }

    public void onGuildReady(@NotNull GuildReadyEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onGuildReady(event);
        }
        GuildDB db = new GuildDB(event.getGuild());
        db.create();
        OusuCore.getEventManager().callEvent(new BotReadyGuildEvent(event));
    }

    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onGuildJoin(event);
        }
        GuildDB db = new GuildDB(event.getGuild());
        db.create();
        OusuCore.getEventManager().callEvent(new BotJoinEvent(event));
    }

    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onGuildLeave(event);
        }
        GuildDB db = new GuildDB(event.getGuild());
        db.delete();
        OusuCore.getEventManager().callEvent(new BotLeaveEvent(event));
    }

    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onGuildMemberJoin(event);
        }
        OusuCore.getEventManager().callEvent(new MemberJoinEvent(event));
    }

    public void onGuildMemberLeave(@NotNull GuildMemberLeaveEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onGuildMemberLeave(event);
        }
        OusuCore.getEventManager().callEvent(new MemberLeaveEvent(event));
    }

    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onGuildMemberRoleAdd(event);
        }
        OusuCore.getEventManager().callEvent(new MemberRoleEvent(event));
    }

    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onGuildMemberRoleRemove(event);
        }
        OusuCore.getEventManager().callEvent(new MemberRoleEvent(event));
    }

    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event) {
        for (ListenerAdapter adapter : OusuCore.getEventManager().getListenerAdapters()) {
            adapter.onGuildMemberUpdateNickname(event);
        }
        OusuCore.getEventManager().callEvent(new MemberUpdateNameEvent(event));
    }
}
