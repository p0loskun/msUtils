package com.github.minersstudios.msutils.player;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mojang.util.InstantTypeAdapter;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.minersstudios.msutils.MSUtils.getInstance;

public class MuteMap {
    private final File file;
    private final Map<UUID, Params> map = new ConcurrentHashMap<>();
    private final Gson gson;

    public MuteMap() {
        this.file = new File(getInstance().getPluginFolder(), "muted_players.json");
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .setPrettyPrinting()
                .create();
        this.reloadMutes();
    }

    /**
     * Gets mute map
     *
     * @return map with {@link UUID} and its {@link Params}
     */
    public @NotNull Map<UUID, Params> getMap() {
        return Collections.unmodifiableMap(this.map);
    }

    /**
     * Gets mute parameters
     *
     * @param player probably muted player
     * @return creation and expiration {@link Instant}, reason and source of mute
     */
    public @Nullable Params getParams(@NotNull OfflinePlayer player) {
        return this.map.get(player.getUniqueId());
    }

    /**
     * @param player probably muted player
     * @return True if the player is muted
     */
    @Contract("null -> false")
    public boolean isMuted(@Nullable OfflinePlayer player) {
        return player != null && this.map.containsKey(player.getUniqueId());
    }

    /**
     * Adds mute for the player
     *
     * @param player     player who will be muted
     * @param expiration {@link Instant} when the mute will be removed
     * @param reason     mute reason
     * @param source     mute source, could be a player's nickname or CONSOLE
     */
    public void put(
            @NotNull OfflinePlayer player,
            @NotNull Instant expiration,
            @NotNull String reason,
            @NotNull String source
    ) {
        Instant created = Instant.now();
        UUID uuid = player.getUniqueId();

        this.map.put(uuid, new Params(created, expiration, reason, source));
        this.saveFile();
    }

    /**
     * Removes mute from player
     *
     * @param player muted player
     */
    public void remove(@Nullable OfflinePlayer player) {
        if (player == null) return;
        this.map.remove(player.getUniqueId());
        this.saveFile();
    }

    /**
     * Reloads muted_players.yml
     */
    public void reloadMutes() {
        this.map.clear();
        try {
            if (!this.file.exists() && this.file.createNewFile()) {
                this.saveFile();
            }

            String json = Files.readString(this.file.toPath(), StandardCharsets.UTF_8);
            Type mapType = new TypeToken<Map<UUID, Params>>() {}.getType();
            Map<UUID, Params> jsonMap = this.gson.fromJson(json, mapType);

            this.map.putAll(jsonMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to reload muted players", e);
        }
    }

    private void saveFile() {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8)) {
            this.gson.toJson(this.map, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save muted players", e);
        }
    }

    public static class Params {
        private final @NotNull Instant created;
        private final @NotNull Instant expiration;
        private final @NotNull String reason;
        private final @NotNull String source;

        public Params(
                @NotNull Instant created,
                @NotNull Instant expiration,
                @NotNull String reason,
                @NotNull String source
        ) {
            this.created = created;
            this.expiration = expiration;
            this.reason = reason;
            this.source = source;
        }

        public @NotNull Instant getCreated() {
            return this.created;
        }

        public @NotNull Instant getExpiration() {
            return this.expiration;
        }

        public @NotNull String getReason() {
            return this.reason;
        }

        public @NotNull String getSource() {
            return this.source;
        }
    }
}
