package me.skiincraft.discord.core.prompt;

import groovy.lang.GroovyShell;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.utils.StringUtils;

import java.util.Collections;

public class EvaluateAction extends ConsoleAction {

    private static final GroovyShell shell = new GroovyShell();
    private static String imports;

    public static void addImport(String pkg){
        imports+= "import " + pkg.concat((pkg.endsWith(".")? "*\n" : ".*\n"));
    }

    public static void setProperty(String property, Object instance){
        shell.setProperty(property, instance);
    }

    public EvaluateAction() {
        super("eval", Collections.singletonList("evaluate"), "eval <code>");
        EvaluateAction.imports =
                "import java.*\n"
                        + "import me.skiincraft.discord.*\n"
                        + "import net.dv8tion.jda.*\n";
    }

    @Override
    boolean execute(String label, String[] args, ConsoleApplication console) {
        if (args.length == 0) {
            OusuCore.getLogger().info("Tente utilizar " + getUsage());
            return false;
        }
        try {
            shell.setProperty("args", args);
            shell.setProperty("shardmanager", OusuCore.getShardManager());

            String script = imports + StringUtils.arrayToString2(0, args).split("\\s+", 2)[0];
            Object saida = shell.evaluate(script);
            OusuCore.getLogger().info("Seu teste foi executado: \n" + (saida == null ? "Não foi possivel fazer este teste :(\n" +StringUtils.arrayToString2(0, args).split("\\s+", 2)[0]: saida.toString()));
        }
        catch (Exception e) {
            OusuCore.getLogger().error("Um problema aconteceu!", e);
            return false;
        }
        return true;
    }
}
