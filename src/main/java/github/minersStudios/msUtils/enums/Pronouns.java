package github.minersStudios.msUtils.enums;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public enum Pronouns {
	HE(
			"зашёл на сервер",
			"вышел из сервера",
			"плюнул",
			"пукнул",
			"тебе",
			"путник",
			"сел",
			"встал",
			"умер"
	),
	SHE(
			"зашла на сервер",
			"вышла из сервера",
			"плюнула",
			"пукнула",
			"тебе",
			"путница",
			"села",
			"встала",
			"умерла"
	),
	THEY(
			"зашли на сервер",
			"вышли из сервера",
			"плюнули",
			"пукнули",
			"вам",
			"путник",
			"сели",
			"встали",
			"умерли"
	);

	@Getter private final String
			joinMessage,
			quitMessage,
			spitMessage,
			fartMessage,
			pronouns,
			traveler,
			sitMessage,
			unSitMessage,
			deathMessage;
	public static final String NAME = ChatColor.DARK_GRAY + "Выберите форму обращения";

	Pronouns(
			String joinMessage,
			String quitMessage,
			String spitMessage,
			String fartMessage,
			String pronouns,
			String traveler,
			String sitMessage,
			String unSitMessage,
			String deathMessage
	) {
		this.joinMessage = joinMessage;
		this.quitMessage = quitMessage;
		this.spitMessage = spitMessage;
		this.fartMessage = fartMessage;
		this.pronouns = pronouns;
		this.traveler = traveler;
		this.sitMessage = sitMessage;
		this.unSitMessage = unSitMessage;
		this.deathMessage = deathMessage;
	}

	/**
	 * @return Pronouns GUI
	 */
	@Nonnull
	public static Inventory getInventory() {
		ItemStack he = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemMeta heMeta = he.getItemMeta();
		assert heMeta != null;
		heMeta.setDisplayName(ChatColor.WHITE + "Он");
		ArrayList<String> loreHe = new ArrayList<>();
		loreHe.add(ChatColor.GRAY + "К вам будут обращаться как к нему");
		heMeta.setLore(loreHe);
		he.setItemMeta(heMeta);

		ItemStack she = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta sheMeta = she.getItemMeta();
		assert sheMeta != null;
		sheMeta.setDisplayName(ChatColor.WHITE + "Она");
		ArrayList<String> loreShe = new ArrayList<>();
		loreShe.add(ChatColor.GRAY + "К вам будут обращаться как к ней");
		sheMeta.setLore(loreShe);
		she.setItemMeta(sheMeta);

		ItemStack they = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta theyMeta = they.getItemMeta();
		assert theyMeta != null;
		theyMeta.setDisplayName(ChatColor.WHITE + "Они");
		ArrayList<String> loreThey = new ArrayList<>();
		loreThey.add(ChatColor.GRAY + "К вам будут обращаться как к ним");
		theyMeta.setLore(loreThey);
		they.setItemMeta(theyMeta);

		Inventory inventory = Bukkit.createInventory(null, 9, NAME);
		inventory.setItem(0, he);
		inventory.setItem(1, he);
		inventory.setItem(2, he);
		inventory.setItem(3, she);
		inventory.setItem(4, she);
		inventory.setItem(5, she);
		inventory.setItem(6, they);
		inventory.setItem(7, they);
		inventory.setItem(8, they);
		return inventory;
	}
}
