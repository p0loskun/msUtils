package com.github.minersstudios.msutils.listeners.entity;

import com.github.minersstudios.msutils.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class EntityDamageListener implements Listener {

	@EventHandler
	public void onEntityDamage(@NotNull EntityDamageEvent event) {
		event.setCancelled(event.getEntity().getWorld() == Main.getWorldDark() && event.getEntity().getType() == EntityType.PLAYER);
	}
}
