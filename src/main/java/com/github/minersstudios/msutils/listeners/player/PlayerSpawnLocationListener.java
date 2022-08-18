package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.Main;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import javax.annotation.Nonnull;

public class PlayerSpawnLocationListener implements Listener {

    @EventHandler
    public void onPlayerSpawnLocation(@Nonnull PlayerSpawnLocationEvent event) {
        if (!event.getPlayer().isDead()) {
            event.setSpawnLocation(new Location(Main.getWorldDark(), 0.0d, 0.0d, 0.0d));
        }
    }
}
