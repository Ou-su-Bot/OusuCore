package me.skiincraft.discord.core.common.reactions.custom;

import me.skiincraft.discord.core.common.reactions.Reaction;
import me.skiincraft.discord.core.common.reactions.ReactionObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactionSelector implements Reaction {

    private final List<EmbedBuilder> embeds;
    private final AtomicInteger page;
    private boolean everyoneReacts;

    public ReactionSelector(List<EmbedBuilder> embeds, boolean everyoneReacts) {
        this.embeds = embeds;
        this.everyoneReacts = everyoneReacts;
        this.page = new AtomicInteger(1);
    }

    @Override
    public boolean execute(TextChannel channel, Member member, String emote, ReactionObject object) {
        // Checando se todos podem reagir ou somente o dono da mensagem.
        if (!isEveryoneReacts() && member.getIdLong() != object.getUserId()){
            return true;
        }
        // Pegando a quantidade de embeds e distribuindo entre os emote
        for (int i = 0; i < embeds.size(); i++){
            if (i >= object.getEmotes().length){
                break;
            }
            String objectEmote = (object.getEmotes()[i].contains(":")) ? object.getEmotes()[i].split(":")[0] : object.getEmotes()[i];
            if (objectEmote.equalsIgnoreCase(emote)){
                // Se a mensagem for a mesma, só retorna e não muda.
                if (page.get() == i + 1){
                    return true;
                }
                page.set(i + 1);
                object.getMessage().editMessage(embeds.get(i).build()).queue();
                return true;
            }
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
