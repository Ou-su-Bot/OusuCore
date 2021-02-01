package me.skiincraft.discord.core.command;

import me.skiincraft.discord.core.command.objecs.Command;

public interface CommandOperation {

    default void onFailure(Exception exception, Command command) {}
    default void onSuccessful(Command command) {}

}
