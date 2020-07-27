package me.skiincraft.discord.core.events.bot;

import me.skiincraft.discord.core.entity.BotTextChannel;
import me.skiincraft.discord.core.entity.BotUser;
import me.skiincraft.discord.core.events.enums.ReactionEventType;
import me.skiincraft.discord.core.utils.Channels;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

/**
 * Evento de reação a mensagens do bot {@link Guild}.
 * <br>Este evento será chamando alguem reagir a alguma mensagem do bot.
 * 
 * Pode ser utilizado para fazer reações interativas.
 */
public class BotReactionEvent extends BotEvent {

	private Emote emote;
	private SelfUser selfUser;
	private String emoji;
	private Message message;
	private String messageId;
	private BotUser user;
	private Guild guild;
	private BotTextChannel botTextChannel;
	private ReactionEventType eventType;
	
	public BotReactionEvent(GuildMessageReactionAddEvent e) {
		ReactionEmote reaction = e.getReactionEmote();
		emote = (reaction.isEmote()) ? reaction.getEmote() : null;
		emoji = (reaction.isEmoji()) ? reaction.getEmoji() : null;
		message = (e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_HISTORY))
				? e.getChannel().getHistory().getMessageById(e.getMessageId())
				: null;
		messageId = e.getMessageId();
		user = Channels.toBotUser(e.getMember());
		guild = e.getGuild();
		botTextChannel = Channels.toBotChannel(e.getChannel());
		eventType = ReactionEventType.ADD;
	}
	
	public BotReactionEvent(GuildMessageReactionRemoveEvent e) {
		ReactionEmote reaction = e.getReactionEmote();
		emote = (reaction.isEmote()) ? reaction.getEmote() : null;
		emoji = (reaction.isEmoji()) ? reaction.getEmoji() : null;
		message = (e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_HISTORY))
				? e.getChannel().getHistory().getMessageById(e.getMessageId())
				: null;
		messageId = e.getMessageId();
		user = Channels.toBotUser(e.getMember());
		guild = e.getGuild();
		botTextChannel = Channels.toBotChannel(e.getChannel());
		eventType = ReactionEventType.REMOVE;
	}
	
	public BotReactionEvent(Emote emote, String emoji, Message message, String messageId, User user, Guild guild,
			BotTextChannel botTextChannel, ReactionEventType eventType) {
		this.emote = emote;
		this.emoji = emoji;
		this.message = message;
		this.messageId = messageId;
		this.user = Channels.toBotUser(user);
		this.guild = guild;
		this.botTextChannel = botTextChannel;
		this.eventType = eventType;
		this.selfUser = guild.getJDA().getSelfUser();
	}
	
	public BotTextChannel getBotTextChannel() {
		return botTextChannel;
	}
	
	public String getEmoji() {
		return emoji;
	}
	public Emote getEmote() {
		return emote;
	}
	public ReactionEventType getEventType() {
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
	public BotUser getUser() {
		return user;
	}
	public SelfUser getSelfUser() {
		return selfUser;
	}

}
