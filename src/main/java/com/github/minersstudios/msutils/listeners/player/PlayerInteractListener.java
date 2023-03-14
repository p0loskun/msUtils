package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerInteractListener implements Listener {

	@EventHandler
	public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (player.getWorld() == MSUtils.getWorldDark()) {
			event.setCancelled(true);
		}

		if (
				event.getAction().isRightClick()
				&& event.getBlockFace() == BlockFace.UP
				&& player.isSneaking()
				&& !player.getPassengers().isEmpty()
		) {
			player.eject();
		}
	}
}
