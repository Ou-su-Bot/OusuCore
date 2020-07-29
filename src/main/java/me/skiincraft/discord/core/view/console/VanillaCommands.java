package me.skiincraft.discord.core.view.console;

import me.skiincraft.discord.core.view.console.commands.RestartCommand;

public final class VanillaCommands {
	
	public static void registerVanillaCommands() {
		ConsoleListener.registerCommand(new RestartCommand());
	}
	
}
