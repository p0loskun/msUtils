package com.github.minersstudios.msutils.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.minersstudios.msutils.MSUtils.getInstance;

@SuppressWarnings("unused")
public class IDMap {
    private final File file;
    private YamlConfiguration configuration;
    private final Map<UUID, Integer> map = new HashMap<>();

    public IDMap() {
        this.file = new File(getInstance().getPluginFolder(), "ids.yml");
        this.reloadIds();
    }

    public int getID(
            @NotNull UUID uuid,
            boolean addPlayer,
            boolean zeroIfNull
    ) {
        return this.map.containsKey(uuid)
                ? this.map.get(uuid)
                : addPlayer
                ? this.addPlayer(uuid)
                : zeroIfNull
                ? 0
                : -1;
    }

    public void setID(
            @NotNull UUID uuid,
            int id
    ) {
        this.map.put(uuid, id);
        this.configuration.set(uuid.toString(), id);
        this.saveFile();
    }

    public int addPlayer(@NotNull UUID uuid) {
        int id = this.nextID();
        this.setID(uuid, id);
        return id;
    }

    public int nextID() {
        Collection<Integer> ids = this.map.values();
        return this.nextID(ids, ids.size());
    }

    private int nextID(
            @NotNull Collection<Integer> ids,
            int id
    ) {
        return ids.contains(id) ? this.nextID(ids, id + 1) : id;
    }

    public @Nullable UUID getUUIDByID(int id) {
        return this.map.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(id))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public @Nullable UUID getUUIDByID(@NotNull String stringId) {
        return this.getUUIDByID(parseID(stringId));
    }

    public @Nullable OfflinePlayer getPlayerByID(int id) {
        UUID uuid = this.getUUIDByID(id);
        return uuid == null ? null : Bukkit.getOfflinePlayer(uuid);
    }

    public @Nullable OfflinePlayer getPlayerByID(@NotNull String stringId) {
        return this.getPlayerByID(this.parseID(stringId));
    }

    public void reloadIds() {
        this.map.clear();
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
        for (Map.Entry<String, Object> entry : this.configuration.getValues(true).entrySet()) {
            this.map.put(UUID.fromString(entry.getKey()), (Integer) entry.getValue());
        }
    }

    public int parseID(@NotNull String stringId) {
        try {
            return Integer.parseInt(stringId);
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }

    private void saveFile() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
