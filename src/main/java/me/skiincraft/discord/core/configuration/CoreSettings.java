package me.skiincraft.discord.core.configuration;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.commons.configuration2.INIConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoreSettings {

    private final String token;
    private final int shards;
    private final ChunkingFilter filter;
    private final List<CacheFlag> cacheFlags;
    private final List<GatewayIntent> gatewayIntents;

    private static final CacheFlag[] DEFAULT_FLAG = new CacheFlag[]{ CacheFlag.EMOTE, CacheFlag.MEMBER_OVERRIDES};
    private static final GatewayIntent[] DEFAULT_INTENT = new GatewayIntent[]{GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS};

    public CoreSettings(String token, int shards, ChunkingFilter filter, List<CacheFlag> cacheFlags, List<GatewayIntent> gatewayIntents) {
        this.token = token;
        this.shards = shards;
        this.filter = filter;
        this.cacheFlags = cacheFlags;
        this.gatewayIntents = gatewayIntents;
    }

    public String getToken() {
        return token;
    }

    public int getShards() {
        if (shards == 0) return 1;
        return shards;
    }

    public ChunkingFilter getFilter() {
        return filter;
    }

    public List<CacheFlag> getCacheFlags() {
        return new ArrayList<>(cacheFlags);
    }

    public List<GatewayIntent> getGatewayIntents() {
        return new ArrayList<>(gatewayIntents);
    }

    public static CoreSettings of(INIConfiguration ini){
        Builder builder = new Builder()
                .setToken(ini.getString("BotConfiguration.token"))
                .setShards(ini.getInt("BotConfiguration.shards"))
                .setFilter(ini.getBoolean("OtherConfiguration.chunkfilter") ? ChunkingFilter.ALL : ChunkingFilter.NONE);

        List<CacheFlag> flags = new ArrayList<>();
        String cacheFlag = ini.getString("OtherConfiguration.cacheflag");
        if (cacheFlag.equalsIgnoreCase("default")){
            flags.addAll(Arrays.asList(DEFAULT_FLAG));
        }

        List<GatewayIntent> intents = new ArrayList<>();
        String gatewayIntents = ini.getString("OtherConfiguration.gatewayintents");
        if (gatewayIntents.equalsIgnoreCase("default")){
            intents.addAll(Arrays.asList(DEFAULT_INTENT));
        }

        return builder.setCacheFlags(flags)
                .setGatewayIntents(intents)
                .createCoreSettings();
    }

    public static class Builder {
        private String token;
        private int shards;
        private ChunkingFilter filter;
        private List<CacheFlag> cacheFlags;
        private List<GatewayIntent> gatewayIntents;

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setShards(int shards) {
            this.shards = shards;
            return this;
        }

        public Builder setFilter(ChunkingFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder setCacheFlags(List<CacheFlag> cacheFlags) {
            this.cacheFlags = cacheFlags;
            return this;
        }

        public Builder setGatewayIntents(List<GatewayIntent> gatewayIntents) {
            this.gatewayIntents = gatewayIntents;
            return this;
        }

        public CoreSettings createCoreSettings() {
            return new CoreSettings(token, shards, filter, cacheFlags, gatewayIntents);
        }
    }

}
