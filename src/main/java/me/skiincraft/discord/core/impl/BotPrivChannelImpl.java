package me.skiincraft.discord.core.impl;

import java.util.function.Consumer;

import me.skiincraft.discord.core.entity.BotPrivChannel;
import me.skiincraft.discord.core.entity.ChannelInteract;
import me.skiincraft.discord.core.entity.ContentMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

public class BotPrivChannelImpl implements BotPrivChannel {

	private PrivateChannel channel;
	private Member member;
	private Message message;
	
	public BotPrivChannelImpl(PrivateChannel channel) {
		this.channel = channel;
	}
	
	public BotPrivChannelImpl(PrivateChannel channel, Member member, Message requisitedMessage) {
		this.channel = channel;
	}
	
	public String getChannelName() {
		return channel.getName();
	}

	public Member getMember() {
		return member;
	}

	public RestAction<Void> addReaction(Emote emote) {
		return message.addReaction(emote);
	}

	public RestAction<Void> addReaction(String emoji) {
		return message.addReaction(emoji);
	}

	public RestAction<Void> addReaction(long messageId, Emote emote) {
		return channel.addReactionById(messageId, emote);
	}

	public RestAction<Void> addReaction(String messageId, String emoji) {
		return channel.addReactionById(messageId, emoji);
	}

	public RestAction<Void> clearReactions() {
		return message.clearReactions();
	}

	public RestAction<Void> removeReaction(Emote emote) {
		return message.removeReaction(emote);
	}

	public RestAction<Void> removeReaction(String emoji) {
		return message.removeReaction(emoji);
	}

	public RestAction<Void> removeReaction(long messageId, Emote emote) {
		return channel.removeReactionById(messageId, emote);
	}

	public RestAction<Void> removeReaction(String messageId, String emoji) {
		return channel.removeReactionById(messageId, emoji);
	}

	public ChannelInteract interact() {
		return new ChannelInteract() {
			
			protected MessageChannel getTextChannel() {
				return getOriginalChannel();
			}
		};
	}
	
	public void reply(String message) {
		interact().reply(message);
	}

	public void reply(MessageEmbed message) {
		interact().reply(message);
	}

	public void reply(ContentMessage message) {
		interact().reply(message);
	}

	public AuditableRestAction<Void> deleteMessage() {
		return message.delete();
	}

	public AuditableRestAction<Void> deleteMessage(long messageId) {
		return channel.deleteMessageById(messageId);
	}

	public AuditableRestAction<Void> deleteMessage(String messageId) {
		return channel.deleteMessageById(messageId);
	}

	public void replyQueue(String message, Consumer<Message> queue) {
		interact().reply(message, queue);
	}

	public void replyQueue(MessageEmbed message, Consumer<Message> queue) {
		interact().reply(message, queue);
	}

	public void replyQueue(ContentMessage message, Consumer<Message> queue) {
		interact().reply(message, queue);
	}

	public JDA getJda() {
		return channel.getJDA();
	}

	public PrivateChannel getOriginalChannel() {
		return channel;
	}

	public User getChannelOwner() {
		return getOriginalChannel().getUser();
	}

}
