package com.github.minersstudios.msutils.player;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public enum ResourcePackType {
	FULL(
			Main.getCachedConfig().fullDropboxUrl,
			Main.getCachedConfig().fullYandexDiskUrl,
			Main.getCachedConfig().fullHash
	),
	LITE(
			Main.getCachedConfig().liteDropboxUrl,
			Main.getCachedConfig().liteYandexDiskUrl,
			Main.getCachedConfig().liteHash
	),
	NONE("", "", "");

	private final String dropBoxURL, yandexDiskURL, hash;
	public static final Component NAME = Component.text("\"Выберите нужный текстурпак").color(NamedTextColor.DARK_GRAY);

	ResourcePackType(
			@NotNull String dropBoxURL,
			@NotNull String yandexDiskURL,
			@NotNull String hash
	) {
		this.dropBoxURL = dropBoxURL;
		this.yandexDiskURL = yandexDiskURL;
		this.hash = hash;
	}

	/**
	 * @param name ResourcePack type name
	 * @return ResourcePackType by name
	 */
	public static @Nullable ResourcePackType getResourcePackByString(@NotNull String name) {
		return switch (name) {
			case "FULL" -> FULL;
			case "LITE" -> LITE;
			case "NONE" -> NONE;
			default -> null;
		};
	}

	/**
	 * @return Resource pack GUI
	 */
	public static @NotNull Inventory getInventory() {
		ItemStack pickRP = new ItemStack(Material.KNOWLEDGE_BOOK);
		ItemMeta pickRPMeta = pickRP.getItemMeta();
		pickRPMeta.displayName(Component.text(ChatColor.WHITE + "Ресурспаки"));
		pickRPMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
		pickRPMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

		ArrayList<Component> lore = new ArrayList<>();
		lore.add(Component.text("Выберите один из").color(NamedTextColor.GRAY));
		lore.add(Component.text("2 видов ресурспаков").color(NamedTextColor.GRAY));
		lore.add(Component.text("или выберите 3 вариант").color(NamedTextColor.GRAY));
		lore.add(Component.text("и играйте без него").color(NamedTextColor.GRAY));
		lore.add(Component.text("(Не рекомендуется)").color(NamedTextColor.GRAY));

		pickRPMeta.lore(lore);
		pickRP.setItemMeta(pickRPMeta);

		ItemStack noneRP = new ItemStack(Material.COAL_BLOCK);
		ItemMeta noneRPMeta = noneRP.getItemMeta();
		noneRPMeta.displayName(Component.text(ChatColor.WHITE + "Без текстурпака"));

		ArrayList<Component> lore0 = new ArrayList<>();
		lore0.add(Component.text("Имеет в себе :").color(NamedTextColor.GRAY));
		lore0.add(Component.text(" - ничего").color(NamedTextColor.GRAY));

		noneRPMeta.lore(lore0);
		noneRP.setItemMeta(noneRPMeta);

		ItemStack liteRP = new ItemStack(Material.IRON_BLOCK);
		ItemMeta liteRPMeta = liteRP.getItemMeta();
		liteRPMeta.displayName(Component.text(ChatColor.WHITE + "Облегчённая версия"));

		ArrayList<Component> lore2 = new ArrayList<>();
		lore2.add(Component.text("Имеет в себе :").color(NamedTextColor.GRAY));
		lore2.add(Component.text(" - текстуры и модельки").color(NamedTextColor.GRAY));
		lore2.add(Component.text("   мебели").color(NamedTextColor.GRAY));
		lore2.add(Component.text(" - текстуры значков").color(NamedTextColor.GRAY));
		lore2.add(Component.text(" - текстуры меню").color(NamedTextColor.GRAY));
		lore2.add(Component.text(" - переименования некоторых").color(NamedTextColor.GRAY));
		lore2.add(Component.text("   предметов").color(NamedTextColor.GRAY));
		lore2.add(Component.text(" - изменённая модель головы").color(NamedTextColor.GRAY));

		liteRPMeta.lore(lore2);
		liteRP.setItemMeta(liteRPMeta);

		ItemStack fullRP = new ItemStack(Material.NETHERITE_BLOCK);
		ItemMeta fullRPMeta = fullRP.getItemMeta();
		fullRPMeta.displayName(Component.text(ChatColor.WHITE + "Полная версия"));

		ArrayList<Component> lore4 = new ArrayList<>();
		lore4.add(Component.text("Имеет в себе :").color(NamedTextColor.GRAY));
		lore4.add(Component.text(" - текстуры и модельки").color(NamedTextColor.GRAY));
		lore4.add(Component.text("   мебели").color(NamedTextColor.GRAY));
		lore4.add(Component.text(" - текстуры значков").color(NamedTextColor.GRAY));
		lore4.add(Component.text(" - текстуры меню").color(NamedTextColor.GRAY));
		lore4.add(Component.text(" - переименования некоторых").color(NamedTextColor.GRAY));
		lore4.add(Component.text("   предметов").color(NamedTextColor.GRAY));
		lore4.add(Component.text(" - изменённая модель головы").color(NamedTextColor.GRAY));
		lore4.add(Component.text(" - OF текстуры и модельки :").color(NamedTextColor.GRAY));
		lore4.add(Component.text("   Небо").color(NamedTextColor.GRAY));
		lore4.add(Component.text("   Некоторые мобы").color(NamedTextColor.GRAY));
		lore4.add(Component.text("   Шапки/Еда/Прочее").color(NamedTextColor.GRAY));
		lore4.add(Component.text(" - анимированные текстуры").color(NamedTextColor.GRAY));
		lore4.add(Component.text("   блоков/предметов").color(NamedTextColor.GRAY));
		lore4.add(Component.text(" - изменённые текстуры/модели").color(NamedTextColor.GRAY));
		lore4.add(Component.text("   блоков/предметов/интерфейса").color(NamedTextColor.GRAY));
		lore4.add(Component.text(" - 3D модель фонаря").color(NamedTextColor.GRAY));
		lore4.add(Component.text(" ").color(NamedTextColor.GRAY));
		lore4.add(Component.text("(" + ChatColor.DARK_RED + "ВНИМАНИЕ!!!" + ChatColor.GRAY + " требует OptiFine/его заменители)").color(NamedTextColor.GRAY));

		fullRPMeta.lore(lore4);
		fullRP.setItemMeta(fullRPMeta);

		Inventory inventory = Bukkit.createInventory(null, 9, NAME);
		inventory.setItem(0, noneRP);
		inventory.setItem(1, noneRP);
		inventory.setItem(2, fullRP);
		inventory.setItem(3, fullRP);
		inventory.setItem(4, pickRP);
		inventory.setItem(5, fullRP);
		inventory.setItem(6, fullRP);
		inventory.setItem(7, liteRP);
		inventory.setItem(8, liteRP);
		return inventory;
	}

	/**
	 * Sets resource pack for player
	 *
	 * @param playerInfo player info
	 */
	public static void setResourcePack(@NotNull PlayerInfo playerInfo) {
		if(playerInfo.getOnlinePlayer() == null) return;
		Player player = playerInfo.getOnlinePlayer();
		if (playerInfo.getResourcePackType() != null) {
			if (playerInfo.getResourcePackType() == ResourcePackType.FULL) {
				player.setResourcePack(ResourcePackType.FULL.getDropBoxURL(), ResourcePackType.FULL.getHash());
			} else if (playerInfo.getResourcePackType() == ResourcePackType.LITE) {
				player.setResourcePack(ResourcePackType.LITE.getDropBoxURL(), ResourcePackType.LITE.getHash());
			} else {
				ChatUtils.sendWarning(player, Component.text("Вы зашли на сервер без ресурспака"));
			}
		} else {
			player.openInventory(ResourcePackType.getInventory());
		}
	}

	public @NotNull String getDropBoxURL() {
		return dropBoxURL;
	}

	public @NotNull String getYandexDiskURL() {
		return yandexDiskURL;
	}

	public @NotNull String getHash() {
		return hash;
	}

	public enum DiskType {
		DROPBOX, YANDEX_DISK
	}
}
