package me.skiincraft.discord.core.events.bot;

import me.skiincraft.discord.core.events.enums.EventType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

import java.util.Objects;

/**
 * Evento de reação a mensagens do bot {@link Guild}.
 * <br>Este evento será chamando alguem reagir a alguma mensagem do bot.
 * 
 * Pode ser utilizado para fazer reações interativas.
 */
public class BotReactionEvent extends BotEvent {

	private final Emote emote;
	private SelfUser selfUser;
	private final String emoji;
	private final Message message;
	private final String messageId;
	private final User user;
	private final Guild guild;
	private final TextChannel textChannel;
	private final EventType eventType;
	
	public BotReactionEvent(GuildMessageReactionAddEvent e) {
		ReactionEmote reaction = e.getReactionEmote();
		emote = (reaction.isEmote()) ? reaction.getEmote() : null;
		emoji = (reaction.isEmoji()) ? reaction.getEmoji() : null;
		message = (e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_HISTORY))
				? e.getChannel().getHistory().getMessageById(e.getMessageId())
				: null;
		messageId = e.getMessageId();
		user = e.getMember().getUser();
		guild = e.getGuild();
		textChannel = e.getChannel();
		eventType = EventType.ADD;
	}
	
	public BotReactionEvent(GuildMessageReactionRemoveEvent e) {
		ReactionEmote reaction = e.getReactionEmote();
		emote = (reaction.isEmote()) ? reaction.getEmote() : null;
		emoji = (reaction.isEmoji()) ? reaction.getEmoji() : null;
		message = (e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_HISTORY))
				? e.getChannel().getHistory().getMessageById(e.getMessageId())
				: null;
		messageId = e.getMessageId();
		user = Objects.requireNonNull(e.getMember()).getUser();
		guild = e.getGuild();
		textChannel = e.getChannel();
		eventType = EventType.REMOVE;
	}
	
	public BotReactionEvent(Emote emote, String emoji, Message message, String messageId, User user, Guild guild,
			TextChannel botTextChannel, EventType eventType) {
		this.emote = emote;
		this.emoji = emoji;
		this.message = message;
		this.messageId = messageId;
		this.user = user;
		this.guild = guild;
		this.textChannel = botTextChannel;
		this.eventType = eventType;
		this.selfUser = guild.getJDA().getSelfUser();
	}
	
	public String getEmoji() {
		return emoji;
	}
	public Emote getEmote() {
		return emote;
	}
	public EventType getEventType() {
		return eventType;
	}
	public Guild getGuild() {
		return guild;
	}
	public Message getMessage() {
		return message;
	}
	public String getMessageId() {
		return messageId;
	}
	
	public TextChannel getTextChannel() {
		return textChannel;
	}
	
	public User getUser() {
		return user;
	}
	
	public SelfUser getSelfUser() {
		return selfUser;
	}

}
