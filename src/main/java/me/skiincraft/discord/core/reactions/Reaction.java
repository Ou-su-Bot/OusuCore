package me.skiincraft.discord.core.reactions;

import java.util.List;

import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.plugin.PluginManager;
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

	
	public Reaction(String name) {
		this.name = name;
	}
	
	public abstract List<ReactionUtil> listHistory();
	
	public abstract void execute(User user, TextChannel channel, ReactionEmote reactionEmote);
	
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if (!isValidReaction(event)) {
			return;
		}
		event.getMessageId();
		execute(event.getUser(), event.getChannel(), event.getReactionEmote());
	}
	
	public GuildMessageReactionAddEvent getEvent() {
		return event;
	}
	
	public String getName() {
		return name;
	}
	
	public ReactionContext getReactionContext() {
		Plugin plugin = PluginManager.getPluginManager().getPluginByBotId(event.getMessageId());
		return new ReactionContext(plugin, event.getChannel(), event.getMessageIdLong());
	}
	
	public ReactionUtil getUtils() {
		return utils;
	}
	
	public String getEmoji() {
		return emoji;
	}
}
