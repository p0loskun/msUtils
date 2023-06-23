package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.listener.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.PlayerInfoMap;
import com.github.minersstudios.msutils.utils.MessageUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerInfoMap playerInfoMap = MSUtils.getConfigCache().playerInfoMap;
        PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(player);
        Entity vehicle = player.getVehicle();

        if (vehicle != null) {
            vehicle.eject();
        }

        event.quitMessage(null);
        playerInfo.unsetSitting();
        MSUtils.getConfigCache().playerAnomalyActionMap.remove(player);
        playerInfo.savePlayerDataParams();

        if (!playerInfo.isInWorldDark()) {
            MessageUtils.sendQuitMessage(playerInfo, player);
        }
    }
}
