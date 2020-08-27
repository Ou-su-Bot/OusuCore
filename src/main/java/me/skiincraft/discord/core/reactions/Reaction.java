package me.skiincraft.discord.core.reactions;

import java.util.List;

import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public abstract class Reaction extends ListenerAdapter {
	
	private String name;
	private ReactionUtil utils;
	private String emoji;
	private GuildMessageReactionAddEvent event;
	private ReactionContext reactionContext;
	public abstract List<ReactionUtil> listHistory();
	
	public Reaction(String name) {
		this.name = name;
	}
	
	public abstract void execute(User user, TextChannel channel, ReactionEmote reactionEmote);
	
	
	public boolean isValidReaction(GuildMessageReactionAddEvent event) {
		List<ReactionUtil> osuhistorys = listHistory();
		if (event.getUser().isBot()) {
			return false;
		}
		if (osuhistorys.isEmpty()) {
			return false;
		}
		ReactionUtil reactionUtil = osuhistorys.stream().filter(o -> o.getMessageId() == event.getMessageIdLong()).findAny().orElse(null);

		if (reactionUtil == null) {
			return false;
		}

		utils = reactionUtil;

		try {
			emoji = event.getReaction().getReactionEmote().getEmoji();
		} catch (IllegalStateException e) {
			return false;
		}

		this.event = event;
		try {
			event.getChannel().removeReactionById(event.getMessageId(), emoji, event.getUser()).queue();
		} catch (InsufficientPermissionException ex) {
			System.out.println("Sem permissão para mudar reaction em: \n" + event.getGuild().getName() + " - "
					+ event.getGuild().getId());
		}
		return true;
	}
	
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if (!isValidReaction(event)) {
			return;
		}
		reactionContext = new ReactionContext(event.getChannel(), event.getMessageIdLong());
		execute(event.getUser(), event.getChannel(), event.getReactionEmote());
	}
	
	public GuildMessageReactionAddEvent getEvent() {
		return event;
	}
	
	public String getName() {
		return name;
	}
	
	public ReactionContext getContext() {
		return reactionContext;
	}
	
	public ReactionUtil getUtils() {
		return utils;
	}
	
	public String getEmoji() {
		return emoji;
	}
}
