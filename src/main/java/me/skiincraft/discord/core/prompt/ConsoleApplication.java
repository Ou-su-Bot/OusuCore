package me.skiincraft.discord.core.prompt;

import me.skiincraft.discord.core.OusuCore;

import java.io.Closeable;
import java.util.*;

public class ConsoleApplication implements Closeable {

    private static ConsoleApplication instance;
    private Scanner scanner;

    private final List<ConsoleAction> consoleActions;

    private ConsoleApplication() {
        consoleActions = new ArrayList<>();
        new Thread(() -> {
            scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String[] next = scanner.nextLine().split(" ");
                if (next.length == 0 || next[0].length() == 0) {
                    continue;
                }

                ConsoleAction action = consoleActions.stream().filter(a -> a.getCommand().equalsIgnoreCase(next[0])).findFirst().orElse(null);
                if (action == null){
                    OusuCore.getLogger().info("Comando desconhecido. digite \"help\" para ajuda.");
                } else {
                    action.execute(next[0], Arrays.stream(next).skip(1).toArray(String[]::new), this);
                }
            }
        }, "Console").start();
        OusuCore.getLogger().info("Console interativo. Digite \"help\" para ajuda.");
        registerVanilla();
    }

    public boolean addConsoleAction(ConsoleAction action){
        return consoleActions.add(action);
    }

    public boolean removeConsoleAction(ConsoleAction action){
        return consoleActions.remove(action);
    }

    @Override
    public void close() {
        scanner.close();
        OusuCore.getLogger().info("Prompt de comando foi fechado.");
    }

    public boolean isClosed(){
        return scanner == null;
    }

    public static ConsoleApplication getInstance() {
        if (instance == null) {
            return instance = new ConsoleApplication();
        }
        return instance;
    }

    private void registerVanilla() {
        addConsoleAction(ConsoleAction.generateOf("exit", (label, args, console) -> {
            OusuCore.shutdown();
            return true;
        }));
        addConsoleAction(new EvaluateAction());
    }
}
