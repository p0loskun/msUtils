package com.github.minersstudios.msutils.chat;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@SuppressWarnings("unused")
public class TranslatableMap {
    private @NotNull String language;
    private @NotNull File file;
    private final @NotNull JsonObject translations;

    /**
     * Default language code
     */
    public static final @NotNull String DEFAULT_LANG = "ru_ru";

    /**
     * Loads language file
     *
     * @param language Language code
     */
    public TranslatableMap(@NotNull String language) {
        this.language = language;
        this.file = this.loadFile();
        this.translations = this.loadTranslations();
    }

    /**
     * @return Language code of this map
     */
    public @NotNull String getLanguage() {
        return this.language;
    }

    /**
     * @return Language keys of this map
     */
    public @NotNull Set<String> getKeys() {
        return this.translations.keySet();
    }

    /**
     * @param key Language key
     * @return True if this map contains specified key
     */
    public boolean containsKey(@NotNull String key) {
        return this.translations.has(key);
    }

    /**
     * @param key Language key
     * @return Value of specified key or key itself if it doesn't exist
     */
    public @NotNull String getValue(@NotNull String key) {
        JsonElement element = this.translations.get(key);
        return element == null ? key : element.getAsString();
    }

    /**
     * Loads language file from plugin folder or from resources if it doesn't exist
     * <br>
     * If language file doesn't exist, default language file will be loaded
     *
     * @return Language file
     * @throws RuntimeException If default language file doesn't exist
     * @see #DEFAULT_LANG
     */
    private @NotNull File loadFile() throws RuntimeException {
        MSUtils plugin = MSUtils.getInstance();
        File langFile = new File(plugin.getPluginFolder(), "/lang/" + this.language + ".json");

        if (!langFile.exists()) {
            try {
                plugin.saveResource("lang/" + this.language + ".json", false);
            } catch (IllegalArgumentException e) {
                if (DEFAULT_LANG.equals(this.language)) {
                    throw new RuntimeException("Failed to load default language file: " + DEFAULT_LANG, e);
                } else {
                    ChatUtils.sendWarning("Language file " + this.language + ".json not found!");
                    ChatUtils.sendWarning("Using default language file " + DEFAULT_LANG + ".json");

                    this.language = DEFAULT_LANG;

                    return this.loadFile();
                }
            }
        }

        return langFile;
    }

    /**
     * Loads language file as JsonObject from file path specified in {@link #file}
     * <br>
     * If language file is invalid, default language file will be loaded
     *
     * @return JsonObject of language file
     * @throws JsonSyntaxException If default language file is invalid
     * @see #DEFAULT_LANG
     */
    private @NotNull JsonObject loadTranslations() throws JsonSyntaxException {
        try {
            Path path = this.file.toPath();
            String content = Files.readString(path);
            JsonElement element = JsonParser.parseString(content);
            return element.getAsJsonObject();
        } catch (IOException | JsonSyntaxException e) {
            if (DEFAULT_LANG.equals(this.language)) {
                throw new JsonSyntaxException("Failed to load default language file: " + this.language + ".json", e);
            } else {
                ChatUtils.sendWarning("Failed to load language file: " + this.language + ".json");
                ChatUtils.sendWarning("Using default language file " + DEFAULT_LANG + ".json");

                this.language = DEFAULT_LANG;
                this.file = this.loadFile();

                return this.loadTranslations();
            }
        }
    }
}
