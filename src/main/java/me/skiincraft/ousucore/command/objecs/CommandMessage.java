package me.skiincraft.ousucore.command.objecs;

import me.skiincraft.ousucore.command.utils.ChannelUtils;
import me.skiincraft.ousucore.exception.ThrowableConsumerException;
import me.skiincraft.ousucore.utils.ThrowableConsumer;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

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

    public void editMessage(CharSequence message){
        editMessage(message, null);
    }

    public void editMessage(Message message){
        editMessage(message, null);
    }

    public void editMessage(MessageEmbed message){
        editMessage(message, null);
    }

    public void editMessage(CharSequence message, ThrowableConsumer<CommandMessage> consumer){
        ChannelUtils.MessageConsumer messageConsumer = new ChannelUtils.MessageConsumer(Thread.currentThread());
        try {
            this.message.editMessage(message).queue(messageConsumer, messageConsumer.ifException());
            if (consumer == null)
                return;

            messageConsumer.waitConsumer();
            if (messageConsumer.error())
                throw messageConsumer.getThrowable();

            consumer.accept(new CommandMessage(messageConsumer.get()));
        } catch (Throwable e) {
            throw new ThrowableConsumerException(e);
        }
    }

    public void editMessage(Message message, ThrowableConsumer<CommandMessage> consumer){
        ChannelUtils.MessageConsumer messageConsumer = new ChannelUtils.MessageConsumer(Thread.currentThread());
        try {
            this.message.editMessage(message).queue(messageConsumer, messageConsumer.ifException());
            if (consumer == null)
                return;

            messageConsumer.waitConsumer();
            if (messageConsumer.error())
                throw messageConsumer.getThrowable();

            consumer.accept(new CommandMessage(messageConsumer.get()));
        } catch (Throwable e) {
            throw new ThrowableConsumerException(e);
        }
    }

    public void editMessage(MessageEmbed message, ThrowableConsumer<CommandMessage> consumer){
        ChannelUtils.MessageConsumer messageConsumer = new ChannelUtils.MessageConsumer(Thread.currentThread());
        try {
            this.message.editMessage(message).queue(messageConsumer, messageConsumer.ifException());
            if (consumer == null)
                return;

            messageConsumer.waitConsumer();
            if (messageConsumer.error())
                throw messageConsumer.getThrowable();

            consumer.accept(new CommandMessage(messageConsumer.get()));
        } catch (Throwable e) {
            throw new ThrowableConsumerException(e);
        }
    }


    public TextChannel getTextChannel(){
        return message.getTextChannel();
    }

    public Message getMessage() {
        return message;
    }
}
