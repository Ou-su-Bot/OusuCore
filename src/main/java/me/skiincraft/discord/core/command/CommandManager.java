package me.skiincraft.discord.core.command;

import java.util.ArrayList;

public class CommandManager {
	
	private ArrayList<Command> commands = new ArrayList<>();
	
	public boolean registerCommand(Command command) {
		return commands.add(command);
	}
	
	public boolean unregisterCommand(Command command) {
		return commands.remove(command);
	}
	
	public boolean unregisterCommand(String command) {
		return commands.remove(commands.stream().filter(c -> c.getCommandName().equalsIgnoreCase(command)).findAny().orElse(null));
	}
	
	public ArrayList<Command> getCommands() {
		return commands;
	}

}
