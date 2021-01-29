package me.skiincraft.discord.core.common.reactions.custom;

import me.skiincraft.discord.core.common.reactions.Reaction;
import me.skiincraft.discord.core.common.reactions.ReactionObject;
import me.skiincraft.discord.core.utils.TriConsumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactionPage implements Reaction {

    private final List<EmbedBuilder> embedBuilders;
    private boolean everyoneReacts;
    private final AtomicInteger page;
    private TriConsumer<Message, Member, EmbedBuilder> onReaction;

    public ReactionPage(List<EmbedBuilder> embedsmessages, boolean everyoneReacts) {
        this.embedBuilders = embedsmessages;
        this.everyoneReacts = everyoneReacts;
        this.page = new AtomicInteger(1);
    }

    public ReactionPage(List<EmbedBuilder> embedsmessages, TriConsumer<Message, Member, EmbedBuilder> onReaction, boolean everyoneReacts) {
        this.embedBuilders = embedsmessages;
        this.everyoneReacts = everyoneReacts;
        this.page = new AtomicInteger(1);
    }

    public boolean execute(TextChannel channel, Member member, String emote, ReactionObject object) {
        if (!isEveryoneReacts() && member.getIdLong() != object.getUserId()){
            return true;
        }

        if (embedBuilders.size() == 0 || embedBuilders.size() == 1) {
            try {
                onReactionAccept(object.getMessage(), member, null);
                object.getMessage().clearReactions().queue();
            } catch (Exception ignored){ return false;}
            return false;
        }

        Message message = object.getMessage();
        if (TimeUnit.MILLISECONDS.toSeconds(object.getDurationTime()) <= 60){
            object.addDurationTime(Duration.ofSeconds(10));
        }

        if (object.getEmotes().length == 1){
            if (object.getEmotes()[0].equalsIgnoreCase(emote)) {
                if (page.intValue() >= embedBuilders.size()) {
                    page.set(1);
                    message.editMessage(onReactionAccept(message, member, embedBuilders.get(0)).build()).queue();
                    return true;
                }
                message.editMessage(onReactionAccept(message, member, embedBuilders.get(page.incrementAndGet() - 1)).build()).queue();
                return true;
            }
        }

        if (object.getEmotes()[0].equalsIgnoreCase(emote)){
            if (page.intValue() <= 1){
                page.set(embedBuilders.size());
                message.editMessage(onReactionAccept(message, member, embedBuilders.get(embedBuilders.size() - 1)).build()).queue();
                return true;
            }
            message.editMessage(onReactionAccept(message, member, embedBuilders.get(page.decrementAndGet() - 1)).build()).queue();
        }

        if (object.getEmotes()[1].equalsIgnoreCase(emote)){
            if (page.intValue() >= embedBuilders.size()){
                page.set(1);
                message.editMessage(embedBuilders.get(0).build()).queue();
                return true;
            }
            message.editMessage(onReactionAccept(message, member, embedBuilders.get(page.incrementAndGet() - 1)).build()).queue();
        }

        return true;
    }

    private EmbedBuilder onReactionAccept(Message message, Member member, EmbedBuilder embedBuilder){
        if (onReaction != null){
            onReaction.accept(message, member, embedBuilder);
            return embedBuilder;
        }
        return embedBuilder;
    }

    public TriConsumer<Message, Member, EmbedBuilder> getOnReaction() {
        return onReaction;
    }

    public ReactionPage setOnReaction(TriConsumer<Message, Member, EmbedBuilder> onReaction) {
        this.onReaction = onReaction;
        return this;
    }

    public void setEveryoneReacts(boolean everyoneReacts) {
        this.everyoneReacts = everyoneReacts;
    }

    public boolean isEveryoneReacts() {
        return everyoneReacts;
    }
}
