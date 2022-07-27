package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import javax.annotation.Nonnull;

@EventListener
public class InventoryCloseListener implements Listener {

	@EventHandler
	public void onInventoryClose(@Nonnull InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (
				event.getView().getTitle().equalsIgnoreCase(ResourcePackType.NAME)
				&& playerInfo.getResourcePackType() == null
		) Bukkit.getScheduler().runTask(Main.plugin, () -> player.openInventory(ResourcePackType.getInventory()));
		if (
				event.getView().getTitle().equalsIgnoreCase(Pronouns.NAME)
				&& playerInfo.getYamlConfiguration().getString("pronouns") == null
		) Bukkit.getScheduler().runTask(Main.plugin, () -> player.openInventory(Pronouns.getInventory()));
	}
}
