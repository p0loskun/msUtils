package github.minersStudios.msUtils.enums;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public enum ResourcePackType {
	FULL(
			Main.plugin.getConfig().getString("fullDropBoxURL", "https://dropbox.com/"),
			Main.plugin.getConfig().getString("fullYandexDiskURL", "https://disk.yandex.ru/")
	),
	LITE(
			Main.plugin.getConfig().getString("liteDropBoxURL", "https://dropbox.com/"),
			Main.plugin.getConfig().getString("liteYandexDiskURL", "https://disk.yandex.ru/")
	),
	NONE(
			null,
			null
	);

	@Getter private final String dropBoxURL, yandexDiskURL;
	public static final String NAME = ChatColor.DARK_GRAY + "Выберите нужный текстурпак";

	ResourcePackType(String dropBoxURL, String yandexDiskURL){
		this.dropBoxURL = dropBoxURL;
		this.yandexDiskURL = yandexDiskURL;
	}

	/**
	 * @param name ResourcePack type name
	 * @return ResourcePackType by name
	 */
	@Nullable public static ResourcePackType getResourcePackByString(@Nonnull String name){
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
	@Nonnull
	public static Inventory getInventory() {

		//Desc

		ItemStack pickRP = new ItemStack(Material.KNOWLEDGE_BOOK);
		ItemMeta pickRPMeta = pickRP.getItemMeta();
		assert pickRPMeta != null;
		pickRPMeta.setDisplayName(ChatColor.WHITE + "Ресурспаки");
		pickRPMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
		pickRPMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.GRAY + "Выберите один из");
		lore.add(ChatColor.GRAY + "2 видов ресурспаков");
		lore.add(ChatColor.GRAY + "или выберите 3 вариант");
		lore.add(ChatColor.GRAY + "и играйте без него");
		lore.add(ChatColor.GRAY + "(Не рекомендуется)");

		pickRPMeta.setLore(lore);
		pickRP.setItemMeta(pickRPMeta);

		//None

		ItemStack noneRP = new ItemStack(Material.COAL_BLOCK);
		ItemMeta noneRPMeta = noneRP.getItemMeta();
		assert noneRPMeta != null;
		noneRPMeta.setDisplayName(ChatColor.WHITE + "Без текстурпака");

		ArrayList<String> lore0 = new ArrayList<>();
		lore0.add(ChatColor.GRAY + "Имеет в себе :");
		lore0.add(ChatColor.GRAY + " - ничего");

		noneRPMeta.setLore(lore0);
		noneRP.setItemMeta(noneRPMeta);

		//Lite

		ItemStack liteRP = new ItemStack(Material.IRON_BLOCK);
		ItemMeta liteRPMeta = liteRP.getItemMeta();
		assert liteRPMeta != null;
		liteRPMeta.setDisplayName(ChatColor.WHITE + "Облегчённая версия");

		ArrayList<String> lore2 = new ArrayList<>();
		lore2.add(ChatColor.GRAY + "Имеет в себе :");
		lore2.add(ChatColor.GRAY + " - текстуры и модельки");
		lore2.add(ChatColor.GRAY + "   мебели");
		lore2.add(ChatColor.GRAY + " - текстуры значков");
		lore2.add(ChatColor.GRAY + " - текстуры меню");
		lore2.add(ChatColor.GRAY + " - переименования некоторых");
		lore2.add(ChatColor.GRAY + "   предметов");
		lore2.add(ChatColor.GRAY + " - изменённая модель головы");

		liteRPMeta.setLore(lore2);
		liteRP.setItemMeta(liteRPMeta);

		//Full

		ItemStack fullRP = new ItemStack(Material.NETHERITE_BLOCK);
		ItemMeta fullRPMeta = fullRP.getItemMeta();
		assert fullRPMeta != null;
		fullRPMeta.setDisplayName(ChatColor.WHITE + "Полная версия");

		ArrayList<String> lore4 = new ArrayList<>();
		lore4.add(ChatColor.GRAY + "Имеет в себе :");
		lore4.add(ChatColor.GRAY + " - текстуры и модельки");
		lore4.add(ChatColor.GRAY + "   мебели");
		lore4.add(ChatColor.GRAY + " - текстуры значков");
		lore4.add(ChatColor.GRAY + " - текстуры меню");
		lore4.add(ChatColor.GRAY + " - переименования некоторых");
		lore4.add(ChatColor.GRAY + "   предметов");
		lore4.add(ChatColor.GRAY + " - изменённая модель головы");
		lore4.add(ChatColor.GRAY + " - OF текстуры и модельки :");
		lore4.add(ChatColor.GRAY + "   Небо");
		lore4.add(ChatColor.GRAY + "   Некоторые мобы");
		lore4.add(ChatColor.GRAY + "   Шапки/Еда/Прочее");
		lore4.add(ChatColor.GRAY + " - пушистые листья");
		lore4.add(ChatColor.GRAY + "   (Bushy Leaves)");
		lore4.add(ChatColor.GRAY + " - анимированные текстуры");
		lore4.add(ChatColor.GRAY + "   блоков/предметов");
		lore4.add(ChatColor.GRAY + " - изменённые текстуры/модели");
		lore4.add(ChatColor.GRAY + "   блоков/предметов/интерфейса");
		lore4.add(ChatColor.GRAY + " - 3D модель фонаря");
		lore4.add(ChatColor.GRAY + " - изменённый градиент");
		lore4.add(ChatColor.GRAY + " ");
		lore4.add(ChatColor.GRAY + "(" + ChatColor.DARK_RED + "ВНИМАНИЕ!!!" + ChatColor.GRAY + " требует OptiFine/его заменители)");

		fullRPMeta.setLore(lore4);
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
	public static void setResourcePack(@Nonnull PlayerInfo playerInfo){
		if(playerInfo.getOnlinePlayer() == null) return;
		final Player player = playerInfo.getOnlinePlayer();
		if (playerInfo.getResourcePackType() != null) {
			if (playerInfo.getResourcePackType() == ResourcePackType.FULL) {
				player.setResourcePack(ResourcePackType.FULL.getDropBoxURL());
			} else if (playerInfo.getResourcePackType() == ResourcePackType.LITE) {
				player.setResourcePack(ResourcePackType.LITE.getDropBoxURL());
			} else {
				ChatUtils.sendWarning(player, "Вы зашли на сервер без ресурспака");
			}
		} else {
			player.openInventory(ResourcePackType.getInventory());
		}
	}
}
