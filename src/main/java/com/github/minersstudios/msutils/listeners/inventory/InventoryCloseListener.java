package com.github.minersstudios.msutils.listeners.inventory;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.player.Pronouns;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.ResourcePack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class InventoryCloseListener implements Listener {

	@EventHandler
	public void onInventoryClose(@NotNull InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Component title = event.getView().title();
		if (title.contains(ResourcePack.Menu.NAME)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (playerInfo.getResourcePackType() == null) {
				Bukkit.getScheduler().runTask(MSUtils.getInstance(), () -> player.openInventory(ResourcePack.Menu.getInventory()));
			}
		} else if (title.contains(Pronouns.NAME)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (playerInfo.getYamlConfiguration().getString("pronouns") == null) {
				Bukkit.getScheduler().runTask(MSUtils.getInstance(), () -> player.openInventory(Pronouns.getInventory()));
			}
		}
	}
}
