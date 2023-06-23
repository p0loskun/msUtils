package com.github.minersstudios.msutils.listeners.entity;

import com.github.minersstudios.mscore.listener.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class EntityDamageListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(@NotNull EntityDamageEvent event) {
        if (
                event.getEntity().getWorld().equals(MSUtils.getWorldDark())
                && event.getEntity().getType() == EntityType.PLAYER
        ) {
            event.setCancelled(true);
        }
    }
}
