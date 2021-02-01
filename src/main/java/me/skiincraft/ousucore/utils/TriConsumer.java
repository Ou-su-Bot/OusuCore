package me.skiincraft.ousucore.utils;

@FunctionalInterface
public interface TriConsumer<K, V, S> {
    void accept(K t, V v, S s);
}
