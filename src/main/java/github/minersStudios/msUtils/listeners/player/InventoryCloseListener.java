package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import javax.annotation.Nonnull;

public class InventoryCloseListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryClose(@Nonnull InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		String title = ChatUtils.legacyComponentSerialize(event.getView().title());
		if (title.equalsIgnoreCase(ResourcePackType.NAME)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (playerInfo.getResourcePackType() == null)
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> player.openInventory(ResourcePackType.getInventory()));
		} else if (title.equalsIgnoreCase(Pronouns.NAME)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (playerInfo.getYamlConfiguration().getString("pronouns") == null)
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> player.openInventory(Pronouns.getInventory()));
		}
	}
}
