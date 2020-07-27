package me.skiincraft.discord.core.entity;

import java.util.function.Consumer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

public interface BotPrivChannel {
	
	PrivateChannel getOriginalChannel();
	User getChannelOwner();
	
	RestAction<Void> addReaction(Emote emote);
	RestAction<Void> addReaction(String emoji);
	RestAction<Void> addReaction(long messageId, Emote emote);
	RestAction<Void> addReaction(String messageId, String emoji);
	
	RestAction<Void> removeReaction(Emote emote);
	RestAction<Void> removeReaction(String emoji);
	RestAction<Void> removeReaction(long messageId, Emote emote);
	RestAction<Void> removeReaction(String messageId, String emoji);
	
	void reply(String message);
	void reply(MessageEmbed message);
	void reply(ContentMessage message);
	
	AuditableRestAction<Void> deleteMessage();
	AuditableRestAction<Void> deleteMessage(long messageId);
	AuditableRestAction<Void> deleteMessage(String messageId);
	
	void replyQueue(String message, Consumer<Message> queue);
	void replyQueue(MessageEmbed message, Consumer<Message> queue);
	void replyQueue(ContentMessage message, Consumer<Message> queue);
	
	JDA getJda();

}
