package com.github.minersstudios.msutils.listeners.inventory;

import com.github.minersstudios.msutils.player.Pronouns;
import com.github.minersstudios.msutils.player.ResourcePackType;
import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.player.PlayerInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

public class InventoryCloseListener implements Listener {

	@EventHandler
	public void onInventoryClose(@NotNull InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Component title = event.getView().title();
		if (title.contains(ResourcePackType.NAME)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (playerInfo.getResourcePackType() == null) {
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> player.openInventory(ResourcePackType.getInventory()));
			}
		} else if (title.contains(Pronouns.NAME)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (playerInfo.getYamlConfiguration().getString("pronouns") == null) {
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> player.openInventory(Pronouns.getInventory()));
			}
		}
	}
}
