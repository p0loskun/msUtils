package com.github.minersstudios.msutils.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
			"умер",
			"убил",
			"сказал"
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
			"умерла",
			"убила",
			"сказала"
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
			"умерли",
			"убили",
			"сказали"
	);

	private final String
			joinMessage,
			quitMessage,
			spitMessage,
			fartMessage,
			pronouns,
			traveler,
			sitMessage,
			unSitMessage,
			deathMessage,
			killMessage,
			saidMessage;
	public static final String INVENTORY_NAME = ChatColor.DARK_GRAY + "Выберите форму обращения";

	Pronouns(
			String joinMessage,
			String quitMessage,
			String spitMessage,
			String fartMessage,
			String pronouns,
			String traveler,
			String sitMessage,
			String unSitMessage,
			String deathMessage,
			String killMessage,
			String saidMessage
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
		this.killMessage = killMessage;
		this.saidMessage = saidMessage;
	}

	/**
	 * @return Pronouns GUI
	 */
	@Nonnull
	public static Inventory getInventory() {
		ItemStack he = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemMeta heMeta = he.getItemMeta();
		assert heMeta != null;
		heMeta.displayName(Component.text(ChatColor.WHITE + "Он"));
		ArrayList<Component> loreHe = new ArrayList<>();
		loreHe.add(Component.text("К вам будут обращаться как к нему").color(NamedTextColor.GRAY));
		heMeta.lore(loreHe);
		he.setItemMeta(heMeta);

		ItemStack she = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta sheMeta = she.getItemMeta();
		assert sheMeta != null;
		sheMeta.displayName(Component.text(ChatColor.WHITE + "Она"));
		ArrayList<Component> loreShe = new ArrayList<>();
		loreShe.add(Component.text("К вам будут обращаться как к ней").color(NamedTextColor.GRAY));
		sheMeta.lore(loreShe);
		she.setItemMeta(sheMeta);

		ItemStack they = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta theyMeta = they.getItemMeta();
		assert theyMeta != null;
		theyMeta.displayName(Component.text(ChatColor.WHITE + "Они"));
		ArrayList<Component> loreThey = new ArrayList<>();
		loreThey.add(Component.text("К вам будут обращаться как к ним").color(NamedTextColor.GRAY));
		theyMeta.lore(loreThey);
		they.setItemMeta(theyMeta);

		Inventory inventory = Bukkit.createInventory(null, 9, Component.text(INVENTORY_NAME));
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


	public String getJoinMessage() {
		return this.joinMessage;
	}

	public String getQuitMessage() {
		return this.quitMessage;
	}

	public String getSpitMessage() {
		return this.spitMessage;
	}

	public String getFartMessage() {
		return this.fartMessage;
	}

	public String getPronouns() {
		return this.pronouns;
	}

	public String getTraveler() {
		return this.traveler;
	}

	public String getSitMessage() {
		return this.sitMessage;
	}

	public String getUnSitMessage() {
		return this.unSitMessage;
	}

	public String getDeathMessage() {
		return this.deathMessage;
	}

	public String getKillMessage() {
		return this.killMessage;
	}

	public String getSaidMessage() {
		return this.saidMessage;
	}
}
