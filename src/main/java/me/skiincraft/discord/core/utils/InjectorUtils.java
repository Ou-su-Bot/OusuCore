package me.skiincraft.discord.core.utils;

import me.skiincraft.beans.Injector;
import me.skiincraft.beans.stereotypes.CommandMap;
import me.skiincraft.beans.stereotypes.EventMap;
import me.skiincraft.beans.stereotypes.RepositoryMap;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.command.CommandExecutor;
import me.skiincraft.discord.core.common.EventListener;
import me.skiincraft.sql.BasicSQL;
import me.skiincraft.sql.exceptions.RepositoryException;
import me.skiincraft.sql.repository.Repository;

public class InjectorUtils {

    public static void configureInjector(Injector injector){
        injector.onNewInstance((clazz) -> {
                try {
                    Class<? extends Repository> repository = clazz.asSubclass(Repository.class);
                    return BasicSQL.getInstance().registerRepository(repository);
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            return null;
        }, RepositoryMap.class);

        injector.onMap((object) -> OusuCore.registerCommand((CommandExecutor) object), CommandMap.class);
        injector.onMap((object) -> OusuCore.registerListener((EventListener) object), EventMap.class);
    }



}
