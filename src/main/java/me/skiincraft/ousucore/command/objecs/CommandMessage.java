package me.skiincraft.ousucore.command.objecs;

import me.skiincraft.ousucore.command.utils.ChannelUtils;
import me.skiincraft.ousucore.exception.ThrowableConsumerException;
import me.skiincraft.ousucore.utils.ThrowableConsumer;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class CommandMessage {

    private Message message;

    public CommandMessage(Message message) {
        this.message = message;
    }

    public void clearReaction(){
        message.clearReactions().queue();
    }

    public void clearReaction(Emote emote){
        message.clearReactions(emote).queue();
    }

    public void clearReaction(String unicode){
        message.clearReactions(unicode).queue();
    }

    public void addReaction(Emote emote){
        message.addReaction(emote).queue();
    }

    public void addReaction(String unicode){
        message.addReaction(unicode).queue();
    }

    public void delete(){
        message.delete().queue();
    }

    public void editMessage(CharSequence message, ThrowableConsumer<CommandMessage> consumer){
        ChannelUtils.ConsumerMessage consumerMessage = new ChannelUtils.ConsumerMessage();
        this.message.editMessage(message).queue(consumerMessage);
        this.message = consumerMessage.getMessage();
        if (consumer == null){
            return;
        }
        try {
            consumer.accept(this);
        } catch (Exception e){
            throw new ThrowableConsumerException(e);
        }
    }

    public void editMessage(Message message, ThrowableConsumer<CommandMessage> consumer){
        ChannelUtils.ConsumerMessage consumerMessage = new ChannelUtils.ConsumerMessage();
        this.message.editMessage(message).queue(consumerMessage);
        this.message = consumerMessage.getMessage();
        if (consumer == null){
            return;
        }
        try {
            consumer.accept(this);
        } catch (Exception e){
            throw new ThrowableConsumerException(e);
        }
    }

    public void editMessage(MessageEmbed message, ThrowableConsumer<CommandMessage> consumer){
        ChannelUtils.ConsumerMessage consumerMessage = new ChannelUtils.ConsumerMessage();
        this.message.editMessage(message).queue(consumerMessage);
        this.message = consumerMessage.getMessage();
        if (consumer == null){
            return;
        }
        try {
            consumer.accept(this);
        } catch (Exception e){
            throw new ThrowableConsumerException(e);
        }
    }
}
