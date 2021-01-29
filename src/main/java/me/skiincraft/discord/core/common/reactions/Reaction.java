package me.skiincraft.discord.core.common.reactions;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

@FunctionalInterface
public interface Reaction {
    boolean execute(TextChannel channel, Member member, String emote, ReactionObject object);
}
