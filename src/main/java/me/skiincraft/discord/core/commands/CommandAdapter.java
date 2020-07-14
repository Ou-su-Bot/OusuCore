package me.skiincraft.discord.core.commands;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.reflect.FieldUtils;

import me.skiincraft.discord.core.adaptation.GenericUser;
import me.skiincraft.discord.core.database.GuildDB;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.utils.StringUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandAdapter extends ListenerAdapter {
	
	private String prefix;
	private String[] args;
	private Plugin plugin;
	
	private TextChannel channel;
	@SuppressWarnings("unused")
	private Guild guild;
	
	private Command command;

	public CommandAdapter(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public Command getCommand(final ArrayList<Command> list, final String name){
	    return list.stream().filter(o -> o.getCommandName().equalsIgnoreCase(name)).findAny().orElse(null);
	}
	
	public Command getCommandByAliase(final ArrayList<Command> list, final String name){
		return list.stream().filter(o -> o.getAliases().stream().anyMatch(name::equalsIgnoreCase)).findAny().orElse(null);
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
		ArrayList<Command> commands = plugin.getCommands();

		prefix = new GuildDB(plugin, e.getGuild()).get("prefix");
		
		if (args.length == 0) {
			return false;
		}
		
		if (!(args[0].length() >= prefix.length())) {
			return false;
		}
		
		if (getCommand(commands, args[0].substring(prefix.length())) == null) {
			if (getCommandByAliase(commands, args[0].substring(prefix.length())) == null) {
				return false;	
			} else {
				command = getCommandByAliase(commands, args[0].substring(prefix.length()));
			}
		} else {
			command = getCommand(commands, args[0].substring(prefix.length()));
		}
		
		if (!args[0].toLowerCase().startsWith(prefix.toLowerCase())) {
			return false;
		}
		
		if (commands.size() == 0) {
			return false;
		}
		
		channel = e.getChannel();
		guild = e.getGuild();
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
						FieldUtils.writeField(command, "plugin", plugin, true);
						FieldUtils.writeField(command, "channel", channel, true);
						channel.sendTyping().queue();
						command.execute(new GenericUser(event.getMember(), plugin), StringUtils.removeString(args, 0), channel);
						Thread.currentThread().interrupt();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
			}
		};
		Thread commandthread = new Thread(commandrunnable, plugin.getDiscordInfo().getBotname() + "-"+ command.getCommandName() + "Command-" + new Random().nextInt(13));
		commandthread.start();
	}
}
