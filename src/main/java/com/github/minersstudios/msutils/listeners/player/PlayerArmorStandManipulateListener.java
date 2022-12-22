package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.utils.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerArmorStandManipulateListener implements Listener {

	@EventHandler
	public void onPlayerArmorStandManipulate(@NotNull PlayerArmorStandManipulateEvent event) {
		event.setCancelled(PlayerUtils.getSeats().containsValue(event.getRightClicked()));
	}
}
