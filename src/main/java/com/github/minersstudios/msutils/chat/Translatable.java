package com.github.minersstudios.msutils.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;

public class Translatable {

    @Contract(value = " -> fail")
    private Translatable() {
        throw new AssertionError("Utility class");
    }

    /**
     * Gets translatable component from {@link TranslatableMap} by specified key
     *
     * @param key Language key
     * @return Translatable component from specified key
     */
    @Contract(value = "_ -> new")
    public static @NotNull TranslatableComponent create(@NotNull String key) {
        return Component.translatable(key, getConfigCache().translatableMap.getValue(key));
    }

    /**
     * Gets translatable component from {@link TranslatableMap} by specified key with formatted arguments
     *
     * @param key  Language key
     * @param args Arguments for translatable component
     * @return Translatable component from specified key with formatted arguments
     */
    @Contract(value = "_, _ -> new")
    public static @NotNull TranslatableComponent create(
            @NotNull String key,
            @NotNull ComponentLike @NotNull ... args
    ) {
        return Component.translatable(key, getConfigCache().translatableMap.getValue(key), args);
    }
}
