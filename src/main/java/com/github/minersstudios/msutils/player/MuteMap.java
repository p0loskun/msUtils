package com.github.minersstudios.msutils.player;

import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.minersstudios.msutils.MSUtils.getInstance;

public class MuteMap {
    private final File file;
    private YamlConfiguration configuration;
    private final Map<OfflinePlayer, Params> map = new HashMap<>();

    public MuteMap() {
        this.file = new File(getInstance().getPluginFolder(), "muted_players.yml");
        this.reloadMutes();
    }

    public @NotNull Map<OfflinePlayer, Params> getMap() {
        return this.map;
    }

    public @Nullable Params getParams(@NotNull OfflinePlayer player) {
        return this.map.get(player);
    }

    @Contract("null -> false")
    public boolean isMuted(@Nullable OfflinePlayer player) {
        return player != null && this.map.containsKey(player);
    }

    public void put(
            @NotNull OfflinePlayer player,
            @NotNull Date expiration,
            @NotNull String reason,
            @NotNull String source
    ) {
        Date created = new Date();
        String uuidStr = player.getUniqueId().toString();

        Bukkit.getScheduler().runTaskAsynchronously(
                MSUtils.getInstance(),
                () -> this.map.put(player, new Params(created, expiration, reason, source))
        );
        this.configuration.set(uuidStr + ".created", created.getTime());
        this.configuration.set(uuidStr + ".expiration", expiration.getTime());
        this.configuration.set(uuidStr + ".reason", reason);
        this.configuration.set(uuidStr + ".source", source);
        this.saveFile();
    }

    public void remove(@Nullable OfflinePlayer player) {
        if (player == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(
                MSUtils.getInstance(),
                () -> this.map.remove(player)
        );
        this.configuration.set(player.getUniqueId().toString(), null);
        this.saveFile();
    }

    public void reloadMutes() {
        this.map.clear();
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
        for (String key : this.configuration.getKeys(false)) {
            ConfigurationSection section = this.configuration.getConfigurationSection(key);
            assert section != null;
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(key));
            Params params = new Params(
                    new Date(section.getLong("created")),
                    new Date(section.getLong("expiration")),
                    section.getString("reason", "неизвестно"),
                    section.getString("source", "CONSOLE")
            );
            Bukkit.getScheduler().runTaskAsynchronously(
                    MSUtils.getInstance(),
                    () -> this.map.put(player, params)
            );
        }
    }

    private void saveFile() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public record Params(
            @NotNull Date getCreated,
            @NotNull Date getExpiration,
            @NotNull String getReason,
            @NotNull String getSource
    ) {}
}
