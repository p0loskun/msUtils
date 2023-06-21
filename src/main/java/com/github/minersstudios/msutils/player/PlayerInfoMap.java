package com.github.minersstudios.msutils.player;

import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class PlayerInfoMap {
    private final Map<UUID, PlayerInfo> map = new HashMap<>();

    public @NotNull Map<UUID, PlayerInfo> getMap() {
        return new HashMap<>(this.map);
    }

    public void put(@NotNull PlayerInfo playerInfo) {
        Bukkit.getScheduler().runTaskAsynchronously(
                MSUtils.getInstance(),
                () -> this.map.put(playerInfo.getUuid(), playerInfo)
        );
    }

    public void remove(@NotNull UUID uniqueId) {
        Bukkit.getScheduler().runTaskAsynchronously(
                MSUtils.getInstance(),
                () -> this.map.remove(uniqueId)
        );
    }

    public void remove(@NotNull PlayerInfo playerInfo) {
        this.remove(playerInfo.getUuid());
    }

    public @NotNull PlayerInfo getPlayerInfo(
            @NotNull UUID uniqueId,
            @NotNull String nickname
    ) {
        PlayerInfo playerInfo = this.map.get(uniqueId);

        if (playerInfo == null) {
            playerInfo = new PlayerInfo(uniqueId, nickname);
            this.put(playerInfo);
        }
        return playerInfo;
    }

    public @NotNull PlayerInfo getPlayerInfo(@NotNull Player player) {
        UUID uniqueId = player.getUniqueId();
        PlayerInfo playerInfo = this.map.get(uniqueId);

        if (playerInfo == null) {
            playerInfo = new PlayerInfo(player);
            this.put(playerInfo);
        }
        return playerInfo;
    }
}
