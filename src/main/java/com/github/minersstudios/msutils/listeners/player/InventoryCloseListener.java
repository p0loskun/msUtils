package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.enums.Pronouns;
import com.github.minersstudios.msutils.enums.ResourcePackType;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.classes.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import javax.annotation.Nonnull;

public class InventoryCloseListener implements Listener {

	@EventHandler
	public void onInventoryClose(@Nonnull InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		String title = ChatUtils.legacyComponentSerialize(event.getView().title());
		if (title.equalsIgnoreCase(ResourcePackType.INVENTORY_NAME)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (playerInfo.getResourcePackType() == null) {
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> player.openInventory(ResourcePackType.getInventory()));
			}
		} else if (title.equalsIgnoreCase(Pronouns.INVENTORY_NAME)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (playerInfo.getYamlConfiguration().getString("pronouns") == null) {
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> player.openInventory(Pronouns.getInventory()));
			}
		}
	}
}
