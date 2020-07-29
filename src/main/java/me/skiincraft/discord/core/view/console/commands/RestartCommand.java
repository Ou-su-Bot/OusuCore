package me.skiincraft.discord.core.view.console.commands;

import java.io.IOException;
import java.util.Arrays;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.view.console.ConsoleCommand;

public class RestartCommand extends ConsoleCommand {

	public RestartCommand() {
		super("restart", Arrays.asList("reiniciar"), "Restart the OusuCore.", false);
	}

	public void execute(String[] args, Plugin interacted) {
		try {
			OusuCore.restart();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
