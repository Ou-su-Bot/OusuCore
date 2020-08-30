package me.skiincraft.discord.core.jda;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.commons.lang3.reflect.FieldUtils;

import me.skiincraft.discord.core.command.Command;
import me.skiincraft.discord.core.configuration.GuildDB;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.utils.StringUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandAdapter extends ListenerAdapter {
	
	private String prefix;
	private String[] args;
	private Plugin plugin;
	
	private TextChannel channel;
	private Command command;

	public CommandAdapter(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public Command getCommand(final ArrayList<Command> list, final String name){
	    return list.stream().filter(o -> o.getCommandName().equalsIgnoreCase(name)).findAny().orElse(null);
	}
	
	public Command getCommandByAliase(final ArrayList<Command> list, final String name){
		return list.stream()
				.filter(c -> {
					if (c.getAliases() == null) {
						return false;
					}
					Stream<String> aliases = c.getAliases().stream().filter(o -> o.equalsIgnoreCase(name));
					if (aliases.count() != 0) {
						return true;
					}
			return false;
		}).findFirst().orElse(null);
	}
	
	private boolean isValidCommand(GuildMessageReceivedEvent e) {
		args = e.getMessage().getContentRaw().split(" ");
		if (e.getAuthor().isBot()) {
			return false;
		}
		if (e.getAuthor().isFake()) {
			return false;
		}
		
		//plugin = PluginManager.getPluginManager().getPluginByBotId(e.getJDA().getSelfUser().getIdLong());
		ArrayList<Command> commands = plugin.getCommandManager().getCommands();

		prefix = new GuildDB(e.getGuild()).get("prefix");
		
		if (args.length == 0) {
			return false;
		}
		
		if (!args[0].toLowerCase().startsWith(prefix)) {
			return false;
		}
		
		if (args[0].length() <= prefix.length()) {
			return false;
		}
		
		command = getCommand(commands, args[0].substring(prefix.length()));
		if (command == null) {
			command = getCommandByAliase(commands, args[0].substring(prefix.length()));
		}
		
		if (command == null) {
			return false;
		}
		
		if (!args[0].toLowerCase().startsWith(prefix.toLowerCase())) {
			return false;
		}
		
		if (commands.size() == 0) {
			return false;
		}
		
		channel = e.getChannel();
		return true;
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (isValidCommand(event) == false) {
			return;
		}
		Runnable commandrunnable = new Runnable() {
			
			@Override
			public void run() {
					try {
						FieldUtils.writeField(command, "channel", event.getChannel(), true);
						channel.sendTyping().queue();
						command.execute(event.getAuthor(), StringUtils.removeString(args, 0), event.getChannel());
						Thread.currentThread().interrupt();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
			}
		};
		Thread commandthread = new Thread(commandrunnable, plugin.getName() + "-"+ command.getCommandName() + "Command-" + new Random().nextInt(13));
		commandthread.start();
	}
}
