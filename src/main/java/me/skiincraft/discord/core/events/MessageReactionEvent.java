package me.skiincraft.discord.core.events;

import me.skiincraft.discord.core.commands.ChannelContext;
import me.skiincraft.discord.core.event.Event;
import me.skiincraft.discord.core.events.objects.ReactionEventType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public class MessageReactionEvent extends Event {

	private ReactionEventType eventType;
	private long messageId;
	private TextChannel channel;
	private Guild guild;
	private MessageReaction messageReaction;
	private ReactionEmote reactionEmote;
	private String emoji;
	private Emote emote;
	private ChannelContext context;

	// I need original event to "parse"
	public MessageReactionEvent(GuildMessageReactionAddEvent e) {
		eventType = ReactionEventType.ADD;
		messageId = e.getMessageIdLong();
		channel = e.getChannel();
		guild = e.getGuild();
		messageReaction = e.getReaction();
		reactionEmote = e.getReactionEmote();
		emoji = e.getReactionEmote().getEmoji();
		emote = e.getReactionEmote().getEmote();
		context = new ChannelContext() {

			public TextChannel getTextChannel() {
				return channel;
			}
		};
	}

	public MessageReactionEvent(GuildMessageReactionRemoveEvent e) {
		eventType = ReactionEventType.REMOVE;
		messageId = e.getMessageIdLong();
		channel = e.getChannel();
		guild = e.getGuild();
		messageReaction = e.getReaction();
		reactionEmote = e.getReactionEmote();
		emoji = e.getReactionEmote().getEmoji();
		emote = e.getReactionEmote().getEmote();
		context = new ChannelContext() {

			public TextChannel getTextChannel() {
				return channel;
			}
		};
	}

	public MessageReactionEvent(GuildMessageReactionRemoveAllEvent e) {
		eventType = ReactionEventType.REMOVEALL;
		messageId = e.getMessageIdLong();
		channel = e.getChannel();
		guild = e.getGuild();
		context = new ChannelContext() {

			public TextChannel getTextChannel() {
				return channel;
			}
		};
	}

	public ReactionEventType getEventType() {
		return eventType;
	}

	public long getMessageId() {
		return messageId;
	}

	public TextChannel getChannel() {
		return channel;
	}

	public Guild getGuild() {
		return guild;
	}

	public MessageReaction getMessageReaction() {
		if (eventType == ReactionEventType.REMOVEALL) {
			return null;
		}
		return messageReaction;
	}

	public ReactionEmote getReactionEmote() {
		if (eventType == ReactionEventType.REMOVEALL) {
			return null;
		}
		return reactionEmote;
	}

	public String getEmoji() {
		if (eventType == ReactionEventType.REMOVEALL) {
			return null;
		}
		return emoji;
	}

	public Emote getEmote() {
		if (eventType == ReactionEventType.REMOVEALL) {
			return null;
		}
		return emote;
	}

	public ChannelContext getContext() {
		return context;
	}

}
