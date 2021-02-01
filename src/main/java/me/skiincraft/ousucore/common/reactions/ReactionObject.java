package me.skiincraft.ousucore.common.reactions;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ReactionObject {

    private final long channelId;
    private final long messageId;
    private final long guildId;
    private final long userId;

    private final Message message;

    private final long createdTime;
    private long durationTime;

    private String[] emotes;

    private ReactionObject(Message message, long userId) {
        this.channelId = message.getChannel().getIdLong();
        this.messageId = message.getIdLong();
        this.guildId = message.getGuild().getIdLong();
        this.userId = userId;
        this.durationTime = TimeUnit.SECONDS.toMillis(60);
        this.createdTime = System.currentTimeMillis();
        this.message = message;
    }

    public ReactionObject(Message message, long userId, @Nonnull String[] emotesUnicode) {
        this(message, userId);
        for (String emote : emotesUnicode){
            message.addReaction(emote).queue();
        }
        this.emotes = emotesUnicode;
    }

    public ReactionObject(Message message, long userId, @Nonnull Emote[] emotes) {
        this(message, userId);
        for (Emote emote : emotes){
            message.addReaction(emote).queue();
        }
        this.emotes = Arrays.stream(emotes).map(Emote::getName).toArray(String[]::new);
    }

    public ReactionObject setDurationTime(Duration duration) {
        this.durationTime = TimeUnit.SECONDS.toMillis(duration.getSeconds());
        return this;
    }

    public ReactionObject addDurationTime(Duration duration) {
        this.durationTime+=TimeUnit.SECONDS.toMillis(duration.getSeconds());
        return this;
    }

    public boolean hasExpired(){
        return durationTime != 0 && (System.currentTimeMillis()-createdTime) >= durationTime;
    }

    public long getChannelId() {
        return channelId;
    }

    public long getGuildId() {
        return guildId;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getUserId() {
        return userId;
    }

    public String[] getEmotes() {
        return emotes;
    }

    public Message getMessage() {
        return message;
    }

    public long getDurationTime() {
        return durationTime;
    }
}
