package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.classes.RegistrationProcess;
import github.minersStudios.msUtils.enums.Crafts;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
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
				if (playerInfo.getResourcePackType() != null && playerInfo.getResourcePackType() != ResourcePackType.NONE) {
					player.kickPlayer(
							ChatColor.RED + "\n§lВы были кикнуты"
							+ ChatColor.DARK_GRAY + "\n\n<---====+====--->"
							+ ChatColor.GRAY + "\nПричина :\n\""
							+ "Этот параметр требует перезахода на сервер" + "\""
							+ ChatColor.DARK_GRAY + "\n<---====+====--->\n"
					);
				}
				playerInfo.setResourcePackType(ResourcePackType.NONE);
				player.closeInventory();
				if (player.getWorld() == Main.worldDark)
					playerInfo.teleportToLastLeaveLocation();
			} else if (event.getSlot() == 2 || event.getSlot() == 3 || event.getSlot() == 5 || event.getSlot() == 6) {
				player.closeInventory();
				playerInfo.setResourcePackType(ResourcePackType.FULL);
				playerInfo.setDiskType(playerInfo.getDiskType());
				player.setResourcePack(ResourcePackType.FULL.getDropBoxURL());
			} else if (event.getSlot() == 7 || event.getSlot() == 8) {
				player.closeInventory();
				playerInfo.setResourcePackType(ResourcePackType.LITE);
				playerInfo.setDiskType(playerInfo.getDiskType());
				player.setResourcePack(ResourcePackType.LITE.getDropBoxURL());
			}
			event.setCancelled(true);
			Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
		}

		if (event.getView().getTitle().equalsIgnoreCase(Pronouns.NAME) && !(event.getClickedInventory() instanceof PlayerInventory)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (event.getSlot() == 0 || event.getSlot() == 1 || event.getSlot() == 2) {
				playerInfo.setPronouns(Pronouns.HE);
				player.closeInventory();
			} else if (event.getSlot() == 3 || event.getSlot() == 4 || event.getSlot() == 5) {
				playerInfo.setPronouns(Pronouns.SHE);
				player.closeInventory();
			} else if (event.getSlot() == 6 || event.getSlot() == 7 || event.getSlot() == 8) {
				playerInfo.setPronouns(Pronouns.THEY);
				player.closeInventory();
			}
			if (playerInfo.getYamlConfiguration().getString("pronouns") != null) {
				new RegistrationProcess().setPronouns(playerInfo);
			} else if (playerInfo.getResourcePackType() != null) {
				playerInfo.teleportToLastLeaveLocation();
			}
			event.setCancelled(true);
			Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
		}

		if (
				event.getView().getTitle().equalsIgnoreCase(Crafts.CRAFTS_NAME)
				&& event.getClickedInventory() != null
				&& !(event.getClickedInventory() instanceof PlayerInventory)
		) {
			ItemStack lastItem = event.getClickedInventory().getItem(35),
					firstItem = event.getClickedInventory().getItem(0);
			if (firstItem != null && !event.getClick().isCreativeAction()) {
				int firstItemIndex = Crafts.getItemIndex(firstItem);
				if (event.getSlot() >= 36 && event.getSlot() <= 39 && firstItemIndex - 35 >= 0) {
					player.openInventory(Crafts.getInventory(firstItemIndex - 35));
				} else if (event.getSlot() == 40) {
					player.closeInventory();
				} else if (event.getSlot() >= 41 && event.getSlot() <= 44 && lastItem != null) {
					player.openInventory(Crafts.getInventory(Crafts.getItemIndex(lastItem)));
				} else if (event.getCurrentItem() != null) {
					Crafts.openCraft(player, event.getCurrentItem(), firstItemIndex);
				}
			}
			event.setCancelled(!event.getClick().isCreativeAction());
			Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
		}

		if (
				event.getView().getTitle().equalsIgnoreCase(Crafts.CRAFT_NAME)
				&& event.getClickedInventory() != null
				&& !(event.getClickedInventory() instanceof PlayerInventory)
		) {
			ItemStack arrow = event.getClickedInventory().getItem(14);
			if (arrow != null && arrow.getItemMeta() != null && event.getSlot() == 40) {
				player.openInventory(Crafts.getInventory(arrow.getItemMeta().getCustomModelData()));
			}
			event.setCancelled(!event.getClick().isCreativeAction());
			Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
		}

		if (player.getWorld() == Main.worldDark) {
			event.setCancelled(true);
			Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
		}

		ItemStack cursor = event.getCursor(), item = event.getCurrentItem();
		if (event.getSlot() == 39 && event.getSlotType() == InventoryType.SlotType.ARMOR) {
			if (cursor != null && item != null && item.getType() == Material.AIR && cursor.getType() != Material.AIR) {
				player.setItemOnCursor(null);
				Bukkit.getScheduler().runTask(Main.plugin, () -> player.getInventory().setHelmet(cursor));
			}
		}

		if (item != null && item.getType() != Material.AIR && event.getClickedInventory() != null) {
			boolean remove = item.getType() == Material.BEDROCK;
			if (!remove)
				for (Enchantment enchantment : item.getEnchantments().keySet())
					remove = item.getEnchantmentLevel(enchantment) > enchantment.getMaxLevel();
			if (remove) {
				event.getClickedInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
				ChatUtils.sendWarning(null, " У игрока : " + player.getName() + " был убран предмет : \n" + item);
				event.setCancelled(true);
				Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
			}
		}
	}
}
