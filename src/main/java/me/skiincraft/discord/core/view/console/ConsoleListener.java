package me.skiincraft.discord.core.view.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.plugin.PluginManager;
import me.skiincraft.discord.core.utils.StringUtils;

public class ConsoleListener {
	
	private static ArrayList<ConsoleCommand> commands = new ArrayList<>();
	
	public static void registerCommand(ConsoleCommand command) {
		commands.add(command);
	}
	
	public static void executeCommand(String[] args) {
		ConsoleCommand command = commands.stream().filter(o -> o.getName().equalsIgnoreCase(args[0])).findFirst().orElse(null);
		if (command == null) {
			command = commands.stream().filter(o -> o.getAliases().stream().filter(i -> i.equalsIgnoreCase(args[0]))
					.findFirst().orElse(null) != null).findFirst().orElse(null);
			
			if (command == null) {
				System.out.println("Comando inexistente");
				return;
			}
		}
		
		Plugin plugin = null;
		//Plugin plugin = checkPlugin(StringUtils.removeString(args, 0));
		
		
		command.execute(StringUtils.removeString(args, 0), (plugin == null) ? null : plugin);
	}
	
	protected static Plugin checkPlugin(String[] args){
		List<String> l = Arrays.asList(args);
		Plugin plugininteract = PluginManager.getPluginManager().getLoadedPlugins().stream().filter(o -> l.stream()
				.filter(b -> b.equalsIgnoreCase(o.getDiscordInfo().getBotname())).findFirst().orElse(null) != null)
				.findFirst().orElse(null);

		return plugininteract;
	}

}
