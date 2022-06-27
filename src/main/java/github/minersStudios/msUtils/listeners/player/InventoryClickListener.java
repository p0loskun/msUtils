package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.classes.RegistrationProcess;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.enums.ResourcePackType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;

public class InventoryClickListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(@Nonnull InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		if (event.getView().getTitle().equalsIgnoreCase(ResourcePackType.NAME) && !(event.getClickedInventory() instanceof PlayerInventory)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());

			if (event.getSlot() == 0 || event.getSlot() == 1) {
				playerInfo.setResourcePackType(ResourcePackType.NONE);
				if(player.getWorld() == Main.worldDark){
					playerInfo.teleportToLastLeaveLocation();
				}
				player.closeInventory();
				if(playerInfo.getResourcePackType() != null && playerInfo.getResourcePackType() != ResourcePackType.NONE){
					player.kickPlayer(
							ChatColor.RED + "\n§lВы были кикнуты"
									+ ChatColor.DARK_GRAY + "\n\n<---====+====--->"
									+ ChatColor.GRAY + "\nПричина :\n\""
									+ "Этот параметр требует перезахода на сервер"
									+ "\""
									+ ChatColor.DARK_GRAY + "\n<---====+====--->\n"
					);
				}
			} else if (event.getSlot() == 2 || event.getSlot() == 3 || event.getSlot() == 5 || event.getSlot() == 6) {
				playerInfo.setResourcePackType(ResourcePackType.FULL);
				playerInfo.setDiskType(playerInfo.getDiskType());
				player.setResourcePack(ResourcePackType.FULL.getDropBoxURL());
			} else if (event.getSlot() == 7 || event.getSlot() == 8) {
				playerInfo.setResourcePackType(ResourcePackType.LITE);
				playerInfo.setDiskType(playerInfo.getDiskType());
				player.setResourcePack(ResourcePackType.LITE.getDropBoxURL());
			}
			event.setCancelled(true);
			player.updateInventory();
		}

		if (event.getView().getTitle().equalsIgnoreCase(Pronouns.NAME) && !(event.getClickedInventory() instanceof PlayerInventory)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());

			if (event.getSlot() == 0 || event.getSlot() == 1 || event.getSlot() == 2) {
				playerInfo.setPronouns(Pronouns.HE);
			} else if (event.getSlot() == 3 || event.getSlot() == 4 || event.getSlot() == 5) {
				playerInfo.setPronouns(Pronouns.SHE);
			} else if (event.getSlot() == 6 || event.getSlot() == 7 || event.getSlot() == 8) {
				playerInfo.setPronouns(Pronouns.THEY);
			}
			if (playerInfo.getResourcePackType() == null) {
				new RegistrationProcess().setPronouns(playerInfo);
			} else {
				playerInfo.teleportToLastLeaveLocation();
			}
			player.closeInventory();
			event.setCancelled(true);
			player.updateInventory();
		}

		if(player.getWorld() == Main.worldDark){
			event.setCancelled(true);
			player.updateInventory();
		}

		if (event.getSlot() == 39 && event.getSlotType() == InventoryType.SlotType.ARMOR) {
			ItemStack cursor = event.getCursor(), item = event.getCurrentItem();
			if (cursor != null && item != null && item.getType() == Material.AIR && cursor.getType() != Material.AIR) {
				player.setItemOnCursor(null);
				Bukkit.getScheduler().runTask(Main.plugin, () -> player.getInventory().setHelmet(cursor));
			}
		}
	}
}
