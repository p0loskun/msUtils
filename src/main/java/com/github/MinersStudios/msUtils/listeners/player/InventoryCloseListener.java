package com.github.MinersStudios.msUtils.listeners.player;

import com.github.MinersStudios.msUtils.enums.Pronouns;
import com.github.MinersStudios.msUtils.enums.ResourcePackType;
import com.github.MinersStudios.msUtils.utils.ChatUtils;
import com.github.MinersStudios.msUtils.Main;
import com.github.MinersStudios.msUtils.classes.PlayerInfo;
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
		if (title.equalsIgnoreCase(ResourcePackType.NAME)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (playerInfo.getResourcePackType() == null) {
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> player.openInventory(ResourcePackType.getInventory()));
			}
		} else if (title.equalsIgnoreCase(Pronouns.NAME)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (playerInfo.getYamlConfiguration().getString("pronouns") == null) {
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> player.openInventory(Pronouns.getInventory()));
			}
		}
	}
}
