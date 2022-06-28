package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.enums.ResourcePackType;
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
		if (event.getView().getTitle().equalsIgnoreCase(ResourcePackType.NAME)) {
			if(new PlayerInfo(player.getUniqueId()).getResourcePackType() == null) {
				Bukkit.getScheduler().runTask(Main.plugin, () -> player.openInventory(ResourcePackType.getInventory()));
			}
		}
		if (event.getView().getTitle().equalsIgnoreCase(Pronouns.NAME)) {
			if(new PlayerInfo(player.getUniqueId()).getYamlConfiguration().getString("pronouns") == null) {
				Bukkit.getScheduler().runTask(Main.plugin, () -> player.openInventory(Pronouns.getInventory()));
			}
		}
	}
}
