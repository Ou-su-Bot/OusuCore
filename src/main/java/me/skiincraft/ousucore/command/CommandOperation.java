package me.skiincraft.ousucore.command;

import me.skiincraft.ousucore.command.objecs.Command;

public interface CommandOperation {

    default void onFailure(Exception exception, Command command) {}
    default void onSuccessful(Command command) {}

}
