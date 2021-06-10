package me.skiincraft.ousucore.events;

import me.skiincraft.ousucore.command.CommandExecutor;
import me.skiincraft.ousucore.command.objecs.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import org.jetbrains.annotations.NotNull;

public class CommandExecuteEvent extends Event {

    private final Command command;
    public CommandExecuteEvent(Command command) {
        super(command.getMessage().getJDA(),200);
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public CommandExecutor getCommandExecutor(){
        return getCommand().getCommandExecutor();
    }
}
