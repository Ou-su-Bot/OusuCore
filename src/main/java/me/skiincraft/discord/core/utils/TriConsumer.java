package me.skiincraft.discord.core.utils;

@FunctionalInterface
public interface TriConsumer<K, V, S> {
    void accept(K t, V v, S s);
}
