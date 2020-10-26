package me.skiincraft.discord.core.command;

import java.util.Arrays;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.utils.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class EvalCommand extends Command {
	
	private static String imports; 
	private static final GroovyShell shell = new GroovyShell();
	private final Gson gson;

	public EvalCommand() {
		super("evaluate", Arrays.asList("eval", "testar"), "eval {code}");
		EvalCommand.imports = 
			      "import java.*\n"
				+ "import me.skiincraft.discord.*\n"
				+ "import net.dv8tion.jda.*\n"
				+ "import me.skiincraft.discord.*\n"
				+ "import com.google.gson.*\n";
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	public static void addImport(String pkg){
		imports+="import " + pkg.concat((pkg.endsWith(".")? "*\n" : ".*\n"));
	}

	public static void setProperty(String property, Object instance){
		shell.setProperty(property, instance);
	}



	public void execute(User user, String[] args, TextChannel channel) {
		if (user.getId().equals(OusuCore.getPluginManager().getPlugin().getPluginConfiguration().get("ownerid").toString())) {
			reply("Você não tem permissão para isso");
			return;
		}
        if (args.length == 0) {
        	reply("Sem argumentos necessarios.");
            return;
        }
        try {
            shell.setProperty("args", args);
            shell.setProperty("channel", channel);
            shell.setProperty("jda", channel.getJDA());
            shell.setProperty("guild", channel.getGuild());
            shell.setProperty("member", channel.getGuild().getMember(user));
            shell.setProperty("plugin", OusuCore.getPluginManager().getPlugin());
            shell.setProperty("gson", gson);
            

            String script = imports + StringUtils.arrayToString2(0, args).split("\\s+", 2)[0];
            Object saida = shell.evaluate(script);
            reply("`" + (saida == null ? "Não foi possivel fazer este teste :(\n" +StringUtils.arrayToString2(0, args).split("\\s+", 2)[0]: saida.toString()) +"`");
        }
        catch (Exception e) {
        	reply("`" + e.getMessage() + "`");
        }
	}

}
