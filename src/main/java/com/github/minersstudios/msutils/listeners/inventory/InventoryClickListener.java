package com.github.minersstudios.msutils.listeners.inventory;

import com.github.minersstudios.msutils.player.Pronouns;
import com.github.minersstudios.msutils.player.ResourcePackType;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.RegistrationProcess;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


import static com.github.minersstudios.msutils.player.CraftsMenu.*;

public class InventoryClickListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(@NotNull InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory clickedInventory = event.getClickedInventory();
		Component inventoryTitle = event.getView().title();
		int slot = event.getSlot();
		ItemStack cursorItem = event.getCursor(),
				currentItem = event.getCurrentItem();

		if (clickedInventory == null) return;

		if (
				(clickedInventory.getType() == InventoryType.PLAYER
				&& (event.getClick().isShiftClick() || event.getClick() == ClickType.DOUBLE_CLICK)
				&& (inventoryTitle.contains(ResourcePackType.NAME)
				|| inventoryTitle.contains(Pronouns.NAME)
				|| inventoryTitle.contains(CRAFT_NAME)
				|| inventoryTitle.contains(CRAFTS_NAME)))
				|| player.getWorld() == Main.getWorldDark()
		) {
			event.setCancelled(true);
			Bukkit.getScheduler().runTask(Main.getInstance(), player::updateInventory);
		}

		if (
				slot == 39
				&& event.getSlotType() == InventoryType.SlotType.ARMOR
				&& cursorItem != null
				&& currentItem != null
				&& currentItem.getType() == Material.AIR
				&& cursorItem.getType() != Material.AIR
		) {
			player.setItemOnCursor(null);
			Bukkit.getScheduler().runTask(Main.getInstance(), () -> player.getInventory().setHelmet(cursorItem));
		}

		if (currentItem != null && currentItem.getType() != Material.AIR) {
			boolean remove = currentItem.getType() == Material.BEDROCK;
			if (!remove) {
				for (Enchantment enchantment : currentItem.getEnchantments().keySet()) {
					remove = currentItem.getEnchantmentLevel(enchantment) > enchantment.getMaxLevel();
				}
			}
			if (remove) {
				clickedInventory.setItem(slot, new ItemStack(Material.AIR));
				ChatUtils.sendWarning(null,
						Component.text(" У игрока : ")
								.append(Component.text(player.getName()))
								.append(Component.text(" был убран предмет : \n"))
								.append(Component.text(currentItem.toString()))
				);
				event.setCancelled(true);
				Bukkit.getScheduler().runTask(Main.getInstance(), player::updateInventory);
			}
		}

		if (clickedInventory.getType() != InventoryType.PLAYER) {
			if (inventoryTitle.contains(ResourcePackType.NAME)) {
				PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
				if (slot == 0 || slot == 1) {
					if (playerInfo.getResourcePackType() != null && playerInfo.getResourcePackType() != ResourcePackType.NONE)
						Bukkit.getScheduler().runTask(Main.getInstance(), () -> PlayerUtils.kickPlayer(player, "Вы были кикнуты", "Этот параметр требует перезахода на сервер"));
					playerInfo.setResourcePackType(ResourcePackType.NONE);
					player.closeInventory();
					if (player.getWorld() == Main.getWorldDark())
						playerInfo.teleportToLastLeaveLocation();
				} else if (slot == 2 || slot == 3 || slot == 5 || slot == 6) {
					player.closeInventory();
					playerInfo.setResourcePackType(ResourcePackType.FULL);
					playerInfo.setDiskType(ResourcePackType.DiskType.DROPBOX);
					player.setResourcePack(ResourcePackType.FULL.getDropBoxURL(), ResourcePackType.FULL.getHash());
				} else if (slot == 7 || slot == 8) {
					player.closeInventory();
					playerInfo.setResourcePackType(ResourcePackType.LITE);
					playerInfo.setDiskType(ResourcePackType.DiskType.DROPBOX);
					player.setResourcePack(ResourcePackType.LITE.getDropBoxURL(), ResourcePackType.LITE.getHash());
				}
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
				event.setCancelled(true);
				Bukkit.getScheduler().runTask(Main.getInstance(), player::updateInventory);
			}

			if (inventoryTitle.contains(Pronouns.NAME)) {
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
				Bukkit.getScheduler().runTask(Main.getInstance(), player::updateInventory);
			}

			if (inventoryTitle.contains(CRAFTS_NAME)) {
				ItemStack firstItem = clickedInventory.getItem(0);
				if (firstItem != null && !event.getClick().isCreativeAction()) {
					Category category = Category.getCategory(firstItem);
					if (category == null) return;
					int firstItemIndex = getItemIndex(firstItem, category);
					if (PREVIOUS_PAGE_BUTTON_SLOTS.contains(slot) && firstItemIndex - 35 >= 0) {
						openCategory(player, firstItemIndex - 36, category);
						player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
					} else if (slot == CRAFTS_QUIT_BUTTON) {
						openCategories(player);
						player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
					} else if (NEXT_PAGE_BUTTON_SLOTS.contains(slot) && firstItemIndex + 36 < category.getRecipes().size()) {
						openCategory(player, firstItemIndex + 36, category);
						player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
					} else if (currentItem != null) {
						openCraft(player, currentItem, firstItemIndex, category);
						player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
					}
				}
				event.setCancelled(!event.getClick().isCreativeAction());
				Bukkit.getScheduler().runTask(Main.getInstance(), player::updateInventory);
			}

			if (inventoryTitle.contains(CRAFT_NAME)) {
				Category category = Category.getCategory(clickedInventory.getItem(RESULT_SLOT));
				if (category == null) return;
				ItemStack arrow = clickedInventory.getItem(ARROW_SLOT);
				if (arrow != null && arrow.getItemMeta() != null && slot == CRAFT_QUIT_BUTTON) {
					player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
					openCategory(player, arrow.getItemMeta().getCustomModelData() - 1, category);
				}
				event.setCancelled(!event.getClick().isCreativeAction());
				Bukkit.getScheduler().runTask(Main.getInstance(), player::updateInventory);
			}

			if (inventoryTitle.contains(CATEGORY_NAME)) {
				if (BLOCKS_CATEGORY_SLOTS.contains(slot)) {
					openCategory(player, 0, Category.BLOCKS);
				} else if (DECORS_CATEGORY_SLOTS.contains(slot)) {
					openCategory(player, 0, Category.DECORS);
				} else if (ITEMS_CATEGORY_SLOTS.contains(slot)) {
					openCategory(player, 0, Category.ITEMS);
				}
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
				event.setCancelled(!event.getClick().isCreativeAction());
				Bukkit.getScheduler().runTask(Main.getInstance(), player::updateInventory);
			}
		}
	}
}