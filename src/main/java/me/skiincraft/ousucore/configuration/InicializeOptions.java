package me.skiincraft.ousucore.configuration;

import me.skiincraft.ousucore.command.ICommandManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Arrays;
import java.util.List;

public class InicializeOptions {

    private ICommandManager commandManager;
    private GatewayIntent[] gatewayIntent;
    private CacheFlag[] cacheFlag;
    private ChunkingFilter chunkfilter;
    private String token;
    private int shards;

    public InicializeOptions(ICommandManager commandManager, CoreSettings coreSettings) {
        this.commandManager = commandManager;
        this.gatewayIntent = coreSettings.getGatewayIntents().toArray(new GatewayIntent[0]);
        this.cacheFlag = coreSettings.getCacheFlags().toArray(new CacheFlag[0]);
        this.chunkfilter = coreSettings.getFilter();
        this.token = coreSettings.getToken();
        this.shards = coreSettings.getShards();
    }

    public ICommandManager getCommandManager() {
        return commandManager;
    }

    public List<GatewayIntent> getGatewayIntent() {
        return Arrays.asList(gatewayIntent);
    }

    public List<CacheFlag> getCacheFlag() {
        return Arrays.asList(cacheFlag);
    }

    public ChunkingFilter getChunkfilter() {
        return chunkfilter;
    }

    public String getToken() {
        return token;
    }

    public int getShards() {
        return shards;
    }

    public InicializeOptions setCommandManager(ICommandManager commandManager) {
        this.commandManager = commandManager;
        return this;
    }

    public InicializeOptions setGatewayIntent(GatewayIntent... gatewayIntent) {
        this.gatewayIntent = gatewayIntent;
        return this;
    }

    public InicializeOptions setCacheFlag(CacheFlag... cacheFlag) {
        this.cacheFlag = cacheFlag;
        return this;
    }

    public InicializeOptions setChunkfilter(ChunkingFilter chunkfilter) {
        this.chunkfilter = chunkfilter;
        return this;
    }

    public InicializeOptions setToken(String token) {
        this.token = token;
        return this;
    }

    public InicializeOptions setShards(int shards) {
        this.shards = shards;
        return this;
    }
}
