package com.github.minersstudios.msutils.player;

import com.github.minersstudios.mscore.inventory.CustomInventory;
import com.github.minersstudios.mscore.inventory.InventoryButton;
import com.github.minersstudios.mscore.utils.InventoryUtils;
import com.github.minersstudios.msutils.MSUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.github.minersstudios.mscore.inventory.InventoryButton.playClickSound;
import static com.github.minersstudios.mscore.utils.ChatUtils.createDefaultStyledText;

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

	public static class Menu {

		public static @NotNull CustomInventory create() {
			ItemStack he = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
			ItemMeta heMeta = he.getItemMeta();
			heMeta.displayName(createDefaultStyledText("Он"));
			ArrayList<Component> loreHe = new ArrayList<>();
			loreHe.add(Component.text("К вам будут обращаться как к нему").color(NamedTextColor.GRAY));
			heMeta.lore(loreHe);
			he.setItemMeta(heMeta);

			ItemStack she = new ItemStack(Material.RED_STAINED_GLASS_PANE);
			ItemMeta sheMeta = she.getItemMeta();
			sheMeta.displayName(createDefaultStyledText("Она"));
			ArrayList<Component> loreShe = new ArrayList<>();
			loreShe.add(Component.text("К вам будут обращаться как к ней").color(NamedTextColor.GRAY));
			sheMeta.lore(loreShe);
			she.setItemMeta(sheMeta);

			ItemStack they = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
			ItemMeta theyMeta = they.getItemMeta();
			theyMeta.displayName(createDefaultStyledText("Они"));
			ArrayList<Component> loreThey = new ArrayList<>();
			loreThey.add(Component.text("К вам будут обращаться как к ним").color(NamedTextColor.GRAY));
			theyMeta.lore(loreThey);
			they.setItemMeta(theyMeta);

			CustomInventory customInventory = new CustomInventory("§8Выберите форму обращения", 1);

			InventoryButton heButton = new InventoryButton(he, (event, inventory, button) -> {
				Player player = (Player) event.getWhoClicked();
				PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
				PlayerFile playerFile = playerInfo.getPlayerFile();
				playerFile.setPronouns(Pronouns.HE);
				playerFile.save();
				playClickSound(player);
				player.closeInventory();
				finishSet(playerInfo, player);
			});
			customInventory.setButtonAt(0, heButton);
			customInventory.setButtonAt(1, heButton);
			customInventory.setButtonAt(2, heButton);

			InventoryButton sheButton = new InventoryButton(she, (event, inventory, button) -> {
				Player player = (Player) event.getWhoClicked();
				PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
				PlayerFile playerFile = playerInfo.getPlayerFile();
				playerFile.setPronouns(Pronouns.SHE);
				playerFile.save();
				playClickSound(player);
				player.closeInventory();
				finishSet(playerInfo, player);
			});
			customInventory.setButtonAt(3, sheButton);
			customInventory.setButtonAt(4, sheButton);
			customInventory.setButtonAt(5, sheButton);

			InventoryButton theyButton = new InventoryButton(they, (event, inventory, button) -> {
				Player player = (Player) event.getWhoClicked();
				PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
				PlayerFile playerFile = playerInfo.getPlayerFile();
				playerFile.setPronouns(Pronouns.THEY);
				playerFile.save();
				playClickSound(player);
				player.closeInventory();
				finishSet(playerInfo, player);
			});
			customInventory.setButtonAt(6, theyButton);
			customInventory.setButtonAt(7, theyButton);
			customInventory.setButtonAt(8, theyButton);

			customInventory.setCloseAction(((event, inventory) -> {
				Player player = (Player) event.getPlayer();
				PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
				if (playerInfo.getPlayerFile().getYamlConfiguration().getString("pronouns") == null) {
					Bukkit.getScheduler().runTask(MSUtils.getInstance(), () -> player.openInventory(customInventory));
				}
			}));

			return customInventory;
		}

		public static void open(@NotNull Player player) {
			CustomInventory customInventory = InventoryUtils.getCustomInventory("pronouns");
			if (customInventory == null) return;
			player.openInventory(customInventory);
		}

		private static void finishSet(@NotNull PlayerInfo playerInfo, @NotNull Player player) {
			if (playerInfo.getPlayerFile().getYamlConfiguration().getString("pronouns") != null) {
				new RegistrationProcess().setPronouns(player, playerInfo);
			} else if (playerInfo.getPlayerFile().getPlayerSettings().getResourcePackType() != null) {
				playerInfo.teleportToLastLeaveLocation();
			}
		}
	}
}
