package me.skiincraft.discord.core.common.reactions;

import net.dv8tion.jda.api.entities.Member;

@FunctionalInterface
public interface Reaction {
    boolean execute(Member member, String emote, ReactionObject object);
}
