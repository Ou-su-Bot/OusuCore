package me.skiincraft.discord.core.impl;

import java.util.function.Consumer;

import me.skiincraft.discord.core.entity.BotTextChannel;
import me.skiincraft.discord.core.entity.ChannelInteract;
import me.skiincraft.discord.core.entity.ContentMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

public class BotTextChannelImpl implements BotTextChannel{

	private TextChannel channel;
	private Member member;
	private Message message;
	
	public BotTextChannelImpl(TextChannel channel) {
		this.channel = channel;
	}
	
	public BotTextChannelImpl(TextChannel channel, Member member, Message requisitedMessage) {
		this.channel = channel;
	}
	
	public String getChannelName() {
		return channel.getName();
	}

	public TextChannel getOriginalChannel() {
		return channel;
	}

	public Guild getGuild() {
		return channel.getGuild();
	}

	public Member getMember() {
		return member;
	}

	public boolean canTalk() {
		return channel.canTalk();
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

	public RestAction<Void> clearReactions(String messageId) {
		return channel.clearReactionsById(messageId);
	}

	public RestAction<Void> clearReactions(long messageId) {
		return channel.clearReactionsById(messageId);
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
			
			protected TextChannel getTextChannel() {
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

}
