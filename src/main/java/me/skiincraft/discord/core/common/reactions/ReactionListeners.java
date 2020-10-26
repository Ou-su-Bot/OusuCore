package me.skiincraft.discord.core.common.reactions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ReactionListeners extends ListenerAdapter {

    private final List<ReactionObject> reactionObjects;
    private final Map<ReactionObject, Reaction> reactionsInterface;

    public ReactionListeners() {
        this.reactionObjects = new ArrayList<>();
        this.reactionsInterface = new HashMap<>();
    }

    public boolean contains(ReactionObject chooserObject){
        return reactionObjects.contains(chooserObject) && reactionsInterface.containsKey(chooserObject);
    }

    public void addElement(ReactionObject object, Reaction reactionInterface){
        if (reactionObjects.contains(object) || reactionsInterface.containsKey(object)){
            System.err.println("Você tentou registrar um Reaction ja que existe.");
            return;
        }
        reactionObjects.add(object);
        reactionsInterface.put(object, reactionInterface);
    }

    public void removeElement(ReactionObject object){
        reactionObjects.remove(object);
        reactionsInterface.remove(object);
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (event.getUser().isBot()){
            return;
        }

        Message eventMessage = Objects.requireNonNull(event.getReaction().getTextChannel())
                .retrieveMessageById(event.getMessageIdLong()).complete();

        if (eventMessage.getMember() == null){
            return;
        }

        if (Objects.requireNonNull(eventMessage.getMember(), "User is null").getIdLong() != event.getGuild().getSelfMember().getIdLong()){
            return;
        }

        List<ReactionObject> expires = reactionObjects.stream().filter(ReactionObject::hasExpired).collect(Collectors.toList());
        for (ReactionObject expired : expires){
            removeElement(expired);
        }

        List<ReactionObject> objects = reactionObjects.stream()
                .filter(object -> object.getGuildId() == event.getGuild().getIdLong())
                .filter(object -> object.getChannelId() == event.getChannel().getIdLong())
                .filter(object -> object.getMessageId() == event.getMessageIdLong())
                .collect(Collectors.toList());

        if (objects.size() == 0) {
            return;
        }

        MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();

        List<MessageReaction> emotes = eventMessage.getReactions().stream()
                .filter(MessageReaction::isSelf)
                .collect(Collectors.toList());

        boolean existentEmote = emotes.stream().filter(emote -> reactionEmote.getName().equalsIgnoreCase(emote.getReactionEmote().getName())).count() == 1;
        if (!existentEmote){
            event.getReaction().removeReaction(event.getUser()).queue();
            return;
        }

        event.getReaction().removeReaction(event.getUser()).queue();
        objects.forEach(reactionObject -> {
            if (reactionsInterface.containsKey(reactionObject)){
                Reaction reaction = reactionsInterface.get(reactionObject);
                String emote = (reactionEmote.isEmoji()) ? reactionEmote.getAsCodepoints() : reactionEmote.getName();
                boolean response = reaction.execute(event.getMember(), emote, reactionObject);
                if (!response){
                    removeElement(reactionObject);
                }
            }
        });
    }
}
