package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.classes.RegistrationProcess;
import github.minersStudios.msUtils.enums.Crafts;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class InventoryClickListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInventoryClick(@Nonnull InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory clickedInventory = event.getClickedInventory();
		String inventoryTitle = ChatUtils.plainTextSerializeComponent(event.getView().title());
		int slot = event.getSlot();
		ItemStack cursorItem = event.getCursor(),
				currentItem = event.getCurrentItem();

		if (clickedInventory == null) return;

		if (
				(clickedInventory.getType() == InventoryType.PLAYER
				&& event.getClick().isShiftClick()
				&& (inventoryTitle.equalsIgnoreCase(ResourcePackType.NAME)
				|| inventoryTitle.equalsIgnoreCase(Pronouns.NAME)
				|| inventoryTitle.equalsIgnoreCase(Crafts.CRAFT_NAME)
				|| inventoryTitle.equalsIgnoreCase(Crafts.CRAFTS_NAME)))
				|| player.getWorld() == Main.worldDark
		) {
			event.setCancelled(true);
			Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
		}

		if (slot == 39 && event.getSlotType() == InventoryType.SlotType.ARMOR) {
			if (cursorItem != null && currentItem != null && currentItem.getType() == Material.AIR && cursorItem.getType() != Material.AIR) {
				player.setItemOnCursor(null);
				Bukkit.getScheduler().runTask(Main.plugin, () -> player.getInventory().setHelmet(cursorItem));
			}
		}

		if (currentItem != null && currentItem.getType() != Material.AIR) {
			boolean remove = currentItem.getType() == Material.BEDROCK;
			if (!remove)
				for (Enchantment enchantment : currentItem.getEnchantments().keySet())
					remove = currentItem.getEnchantmentLevel(enchantment) > enchantment.getMaxLevel();
			if (remove) {
				clickedInventory.setItem(slot, new ItemStack(Material.AIR));
				ChatUtils.sendWarning(null,
						Component.text(" У игрока : ")
						.append(Component.text(player.getName()))
						.append(Component.text(" был убран предмет : \n"))
						.append(Component.text(currentItem.toString()))
				);
				event.setCancelled(true);
				Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
			}
		}

		if (clickedInventory.getType() != InventoryType.PLAYER) {
			if (inventoryTitle.equalsIgnoreCase(ResourcePackType.NAME)) {
				PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
				if (slot == 0 || slot == 1) {
					if (playerInfo.getResourcePackType() != null && playerInfo.getResourcePackType() != ResourcePackType.NONE)
						Bukkit.getScheduler().runTask(Main.plugin, () -> PlayerUtils.kickPlayer(player, "Вы были кикнуты", "Этот параметр требует перезахода на сервер"));
					playerInfo.setResourcePackType(ResourcePackType.NONE);
					player.closeInventory();
					if (player.getWorld() == Main.worldDark)
						playerInfo.teleportToLastLeaveLocation();
				} else if (slot == 2 || slot == 3 || slot == 5 || slot == 6) {
					player.closeInventory();
					playerInfo.setResourcePackType(ResourcePackType.FULL);
					playerInfo.setDiskType(ResourcePackType.DiskType.DROPBOX);
					player.setResourcePack(ResourcePackType.FULL.getDropBoxURL(), PlayerUtils.encrypt(ResourcePackType.FULL.getDropBoxURL()));
				} else if (slot == 7 || slot == 8) {
					player.closeInventory();
					playerInfo.setResourcePackType(ResourcePackType.LITE);
					playerInfo.setDiskType(ResourcePackType.DiskType.DROPBOX);
					player.setResourcePack(ResourcePackType.LITE.getDropBoxURL(), PlayerUtils.encrypt(ResourcePackType.LITE.getDropBoxURL()));
				}
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
				event.setCancelled(true);
				Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
			}

			if (inventoryTitle.equalsIgnoreCase(Pronouns.NAME)) {
				PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
				if (slot == 0 || slot == 1 || slot == 2) {
					playerInfo.setPronouns(Pronouns.HE);
					player.closeInventory();
				} else if (slot == 3 || slot == 4 || slot == 5) {
					playerInfo.setPronouns(Pronouns.SHE);
					player.closeInventory();
				} else if (slot == 6 || slot == 7 || slot == 8) {
					playerInfo.setPronouns(Pronouns.THEY);
					player.closeInventory();
				}
				if (playerInfo.getYamlConfiguration().getString("pronouns") != null) {
					new RegistrationProcess().setPronouns(player, playerInfo);
				} else if (playerInfo.getResourcePackType() != null) {
					playerInfo.teleportToLastLeaveLocation();
				}
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
				event.setCancelled(true);
				Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
			}

			if (inventoryTitle.equalsIgnoreCase(Crafts.CRAFTS_NAME)) {
				ItemStack firstItem = clickedInventory.getItem(0);
				if (firstItem != null && !event.getClick().isCreativeAction()) {
					int firstItemIndex = Crafts.getItemIndex(firstItem);
					if (slot >= 36 && slot <= 39 && firstItemIndex - 35 >= 0) {
						player.openInventory(Crafts.getInventory(firstItemIndex - 36));
					} else if (slot == 40) {
						player.closeInventory();
					} else if (slot >= 41 && slot <= 44 && firstItemIndex + 36 < Crafts.values().length) {
						player.openInventory(Crafts.getInventory(firstItemIndex + 36));
					} else if (currentItem != null) {
						Crafts.openCraft(player, currentItem, firstItemIndex);
					}
				}
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
				event.setCancelled(!event.getClick().isCreativeAction());
				Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
			}

			if (inventoryTitle.equalsIgnoreCase(Crafts.CRAFT_NAME)) {
				ItemStack arrow = clickedInventory.getItem(14);
				if (arrow != null && arrow.getItemMeta() != null && slot == 31) {
					player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
					player.openInventory(Crafts.getInventory(arrow.getItemMeta().getCustomModelData()));
				}
				event.setCancelled(!event.getClick().isCreativeAction());
				Bukkit.getScheduler().runTask(Main.plugin, player::updateInventory);
			}
		}
	}
}
