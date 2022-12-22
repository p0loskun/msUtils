package com.github.minersstudios.msutils.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

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

	private final @NotNull String
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
	public static final Component NAME = Component.text("Выберите форму обращения").color(NamedTextColor.DARK_GRAY);

	Pronouns(
			@NotNull String joinMessage,
			@NotNull String quitMessage,
			@NotNull String spitMessage,
			@NotNull String fartMessage,
			@NotNull String pronouns,
			@NotNull String traveler,
			@NotNull String sitMessage,
			@NotNull String unSitMessage,
			@NotNull String deathMessage,
			@NotNull String killMessage,
			@NotNull String saidMessage
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
	public static @NotNull Inventory getInventory() {
		ItemStack he = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemMeta heMeta = he.getItemMeta();
		heMeta.displayName(Component.text(ChatColor.WHITE + "Он"));
		ArrayList<Component> loreHe = new ArrayList<>();
		loreHe.add(Component.text("К вам будут обращаться как к нему").color(NamedTextColor.GRAY));
		heMeta.lore(loreHe);
		he.setItemMeta(heMeta);

		ItemStack she = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta sheMeta = she.getItemMeta();
		sheMeta.displayName(Component.text(ChatColor.WHITE + "Она"));
		ArrayList<Component> loreShe = new ArrayList<>();
		loreShe.add(Component.text("К вам будут обращаться как к ней").color(NamedTextColor.GRAY));
		sheMeta.lore(loreShe);
		she.setItemMeta(sheMeta);

		ItemStack they = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta theyMeta = they.getItemMeta();
		theyMeta.displayName(Component.text(ChatColor.WHITE + "Они"));
		ArrayList<Component> loreThey = new ArrayList<>();
		loreThey.add(Component.text("К вам будут обращаться как к ним").color(NamedTextColor.GRAY));
		theyMeta.lore(loreThey);
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


	public @NotNull String getJoinMessage() {
		return this.joinMessage;
	}

	public @NotNull String getQuitMessage() {
		return this.quitMessage;
	}

	public @NotNull String getSpitMessage() {
		return this.spitMessage;
	}

	public @NotNull String getFartMessage() {
		return this.fartMessage;
	}

	public @NotNull String getPronouns() {
		return this.pronouns;
	}

	public @NotNull String getTraveler() {
		return this.traveler;
	}

	public @NotNull String getSitMessage() {
		return this.sitMessage;
	}

	public @NotNull String getUnSitMessage() {
		return this.unSitMessage;
	}

	public @NotNull String getDeathMessage() {
		return this.deathMessage;
	}

	public @NotNull String getKillMessage() {
		return this.killMessage;
	}

	public @NotNull String getSaidMessage() {
		return this.saidMessage;
	}
}
