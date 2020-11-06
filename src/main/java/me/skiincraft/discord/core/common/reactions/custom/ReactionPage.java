package me.skiincraft.discord.core.common.reactions.custom;

import me.skiincraft.discord.core.common.reactions.Reaction;
import me.skiincraft.discord.core.common.reactions.ReactionObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactionPage implements Reaction {

    private final List<EmbedBuilder> embedBuilders;
    private boolean everyoneReacts;
    private final AtomicInteger page;

    public ReactionPage(List<EmbedBuilder> embedsmessages, boolean everyoneReacts) {
        this.embedBuilders = embedsmessages;
        this.everyoneReacts = everyoneReacts;
        this.page = new AtomicInteger(1);
    }


    public boolean execute(Member member, String emote, ReactionObject object) {
        if (!isEveryoneReacts() && member.getIdLong() != object.getUserId()){
            return true;
        }

        if (embedBuilders.size() == 0 || embedBuilders.size() == 1) {
            try {
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
                    message.editMessage(embedBuilders.get(0).build()).queue();
                    return true;
                }
                message.editMessage(embedBuilders.get(page.incrementAndGet() - 1).build()).queue();
                return true;
            }
        }

        if (object.getEmotes()[0].equalsIgnoreCase(emote)){
            if (page.intValue() <= 1){
                page.set(embedBuilders.size());
                message.editMessage(embedBuilders.get(embedBuilders.size() - 1).build()).queue();
                return true;
            }
            message.editMessage(embedBuilders.get(page.decrementAndGet() - 1).build()).queue();
        }

        if (object.getEmotes()[1].equalsIgnoreCase(emote)){
            if (page.intValue() >= embedBuilders.size()){
                page.set(1);
                message.editMessage(embedBuilders.get(0).build()).queue();
                return true;
            }
            message.editMessage(embedBuilders.get(page.incrementAndGet() - 1).build()).queue();
        }

        return true;
    }

    public void setEveryoneReacts(boolean everyoneReacts) {
        this.everyoneReacts = everyoneReacts;
    }

    public boolean isEveryoneReacts() {
        return everyoneReacts;
    }
}
