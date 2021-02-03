package me.skiincraft.ousucore.events;

import me.skiincraft.ousucore.command.CommandExecutor;
import me.skiincraft.ousucore.command.objecs.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import org.jetbrains.annotations.NotNull;

public class CommandExecuteEvent implements GenericEvent {

    private Command command;
    private JDA jda;

    public CommandExecuteEvent(Command command, JDA jda) {
        this.command = command;
        this.jda = jda;
    }


    public Command getCommand() {
        return command;
    }

    public CommandExecutor getCommandExecutor(){
        return getCommand().getCommandExecutor();
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return jda;
    }

    @Override
    public long getResponseNumber() {
        return 200;
    }
}
