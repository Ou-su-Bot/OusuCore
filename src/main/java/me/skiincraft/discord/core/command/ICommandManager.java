package me.skiincraft.discord.core.command;

import me.skiincraft.discord.core.command.objecs.Command;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface ICommandManager {

    void registerCommand(CommandExecutor cmd);
    void unregisterCommand(CommandExecutor cmd);

    List<CommandExecutor> getCommands();

    default List<CommandExecutor> getCommands(Predicate<CommandExecutor> predicate){
        return getCommands().stream().filter(predicate).collect(Collectors.toList());
    }

    default <T extends CommandExecutor> List<CommandExecutor> getCommands(Class<T> cast, Predicate<CommandExecutor> predicate){
        return getCommands().stream().filter(cast::isInstance)
                .filter(predicate).collect(Collectors.toList());
    }

    void call(Command command);
}
