package me.skiincraft.discord.core.events.member;

import me.skiincraft.discord.core.command.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.atomic.AtomicBoolean;

public class PreCommandEvent extends MemberEvent {

    private final Message message;
    private final  Member member;
    private final TextChannel channel;
    private final Command command;
    private final AtomicBoolean cancel;

    public PreCommandEvent(Message message, Member member, TextChannel channel, Command command) {
        this.message = message;
        this.member = member;
        this.channel = channel;
        this.command = command;
        cancel = new AtomicBoolean();
    }

    public TextChannel getChannel() {
        return channel;
    }

    public Command getCommand() {
        return command;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public Guild getGuild() {
        return getChannel().getGuild();
    }

    public Message getMessage() {
        return message;
    }

    public boolean isCancelled(){
        return cancel.get();
    }

    public void setCancelled(boolean b){
        cancel.set(b);
    }
}
