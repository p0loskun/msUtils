package com.github.minersstudios.msutils.player;

import com.github.minersstudios.msutils.Main;
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

import static com.github.minersstudios.msutils.utils.ChatUtils.*;

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
		ItemStack pick = new ItemStack(Material.KNOWLEDGE_BOOK);
		ItemMeta pickMeta = pick.getItemMeta();
		pickMeta.displayName(Component.text(ChatColor.WHITE + "Ресурспаки"));
		pickMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
		pickMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		pickMeta.lore(convertStringsToComponents(
				COLORLESS_DEFAULT_STYLE.color(NamedTextColor.GRAY),
				"Выберите один из",
				"2 видов текстурпаков",
				"или выберите 1 вариант",
				"и играйте без него",
				"(Не рекомендуется)"
		));
		pick.setItemMeta(pickMeta);

		ItemStack none = new ItemStack(Material.COAL_BLOCK);
		ItemMeta noneMeta = none.getItemMeta();
		noneMeta.displayName(Component.text(ChatColor.WHITE + "Без текстурпака"));
		noneMeta.lore(convertStringsToComponents(
				COLORLESS_DEFAULT_STYLE.color(NamedTextColor.GRAY),
				"Имеет в себе :",
				" - ничего"
		));
		none.setItemMeta(noneMeta);

		ItemStack lite = new ItemStack(Material.IRON_BLOCK);
		ItemMeta liteMeta = lite.getItemMeta();
		liteMeta.displayName(Component.text(ChatColor.WHITE + "Облегчённая версия"));
		liteMeta.lore(convertStringsToComponents(
				COLORLESS_DEFAULT_STYLE.color(NamedTextColor.GRAY),
				"Имеет в себе :",
				" - текстуры и модельки",
				"   мебели",
				" - текстуры значков",
				" - текстуры меню",
				" - переименования некоторых",
				"   предметов",
				" - изменённая модель головы"
		));
		lite.setItemMeta(liteMeta);

		ItemStack full = new ItemStack(Material.NETHERITE_BLOCK);
		ItemMeta fullMeta = full.getItemMeta();
		fullMeta.displayName(Component.text(ChatColor.WHITE + "Полная версия"));
		fullMeta.lore(convertStringsToComponents(
				COLORLESS_DEFAULT_STYLE.color(NamedTextColor.GRAY),
				"Имеет в себе :",
				" - текстуры и модельки",
				"   мебели",
				" - текстуры значков",
				" - текстуры меню",
				" - переименования некоторых",
				"   предметов",
				" - изменённая модель головы",
				" - OF текстуры и модельки :",
				"   Небо",
				"   Некоторые мобы",
				"   Шапки/Еда/Прочее",
				" - анимированные текстуры",
				"   блоков/предметов",
				" - изменённые текстуры/модели",
				"   блоков/предметов/интерфейса",
				" - 3D модель фонаря"
		));
		full.setItemMeta(fullMeta);

		Inventory inventory = Bukkit.createInventory(null, 9, NAME);
		inventory.setContents(new ItemStack[]{none, none, full, full, pick, full, full, lite, lite});
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
				sendWarning(player, Component.text("Вы зашли на сервер без ресурспака"));
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
