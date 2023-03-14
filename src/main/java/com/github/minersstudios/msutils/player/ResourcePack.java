package com.github.minersstudios.msutils.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.logging.Level;

import static com.github.minersstudios.mscore.utils.ChatUtils.COLORLESS_DEFAULT_STYLE;
import static com.github.minersstudios.mscore.utils.ChatUtils.convertStringsToComponents;
import static com.github.minersstudios.msutils.MSUtils.getConfigCache;
import static com.github.minersstudios.msutils.MSUtils.getInstance;

public class ResourcePack {
	private final @NotNull String hash;
	private final @NotNull String url;

	private static String user;
	private static String repo;
	public static String tagName;

	public ResourcePack(
			@NotNull String hash,
			@NotNull String url
	) {
		this.hash = hash;
		this.url = url;
	}

	public static void init() {
		user = getConfigCache().user;
		repo = getConfigCache().repo;
		Map.Entry<Boolean, String> latestTagName = getLatestTagName();
		tagName = latestTagName.getValue();
		boolean hasNoUpdates =
				tagName.equals(getConfigCache().version)
				&& latestTagName.getKey()
				&& getConfigCache().fullHash != null
				&& getConfigCache().liteHash != null;

		String fullFileName = String.format(getConfigCache().fullFileName, tagName);
		String liteFileName = String.format(getConfigCache().liteFileName, tagName);

		String fullUrl = "https://github.com/" + user + "/" + repo + "/releases/download/" + tagName + "/" + fullFileName;
		String liteUrl = "https://github.com/" + user + "/" + repo + "/releases/download/" + tagName + "/" + liteFileName;

		String fullHash = hasNoUpdates
				? getConfigCache().fullHash
				: generateHash(fullUrl, fullFileName);
		String liteHash = hasNoUpdates
				? getConfigCache().liteHash
				: generateHash(liteUrl, liteFileName);

		Type.FULL.resourcePack = new ResourcePack(fullHash, fullUrl);
		Type.LITE.resourcePack = new ResourcePack(liteHash, liteUrl);

		if (!hasNoUpdates) {
			getConfigCache().configYaml.set("resource-pack.version", tagName);
			getConfigCache().configYaml.set("resource-pack.full.hash", fullHash);
			getConfigCache().configYaml.set("resource-pack.lite.hash", liteHash);

			deleteResourcePackFiles(getConfigCache().fullFileName);
			deleteResourcePackFiles(getConfigCache().liteFileName);

			try {
				getConfigCache().configYaml.save(getConfigCache().configFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static void deleteResourcePackFiles(@NotNull String fileName) {
		File[] files = getInstance().getPluginFolder().listFiles();
		if (files != null) {
			for (File fileFromList : files) {
				String name = fileFromList.getName();
				if (name.matches(String.format(fileName, ".*"))) {
					boolean delete = fileFromList.delete();
					if (!delete) {
						throw new SecurityException("File deletion failed");
					}
				}
			}
		}
	}

	@Contract("_, _ -> new")
	private static @NotNull String generateHash(@NotNull String url, @NotNull String fileName) {
		try {
			ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(url).openStream());
			FileOutputStream out = new FileOutputStream(getInstance().getPluginFolder() + "/" + fileName);
			out.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			out.close();
			readableByteChannel.close();
			return bytesToHexString(createSha1(new File(getInstance().getPluginFolder(), fileName)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static byte @NotNull [] createSha1(@NotNull File file) {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			int n = 0;
			byte[] buffer = new byte[1024];
			while (n != -1) {
				n = fileInputStream.read(buffer);
				if (n > 0) {
					digest.update(buffer, 0, n);
				}
			}
			fileInputStream.close();
			return digest.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static @NotNull String bytesToHexString(byte @NotNull [] bytes) {
		StringBuilder stringBuilder = new StringBuilder();
		for (byte b : bytes) {
			int value = b & 0xFF;
			if (value < 16) {
				stringBuilder.append("0");
			}
			stringBuilder.append(Integer.toHexString(value));
		}
		return stringBuilder.toString();
	}

	private static @NotNull Map.Entry<Boolean, String> getLatestTagName() {
		URI uri = URI.create("https://api.github.com/repos/" + user + "/" + repo + "/tags");
		try {
			String json = IOUtils.toString(uri, StandardCharsets.UTF_8);
			JsonArray tags = JsonParser.parseString(json).getAsJsonArray();
			JsonObject latestTag = tags.get(0).getAsJsonObject();
			return Map.entry(true, latestTag.get("name").getAsString());
		} catch (IOException e) {
			String configTagName = getConfigCache().version;
			if (configTagName == null) {
				throw new RuntimeException("Apparently the API rate limit has been exceeded\nRequest URL : " + uri, e);
			}
			Bukkit.getLogger().log(Level.SEVERE, "Apparently the API rate limit has been exceeded. Plugin will use existing version\nRequest URL : " + uri, e);
			return Map.entry(false, configTagName);
		}
	}

	/**
	 * Sets resource pack for player
	 *
	 * @param playerInfo player info
	 */
	public static void setResourcePack(@NotNull PlayerInfo playerInfo) {
		if (playerInfo.getOnlinePlayer() == null) return;
		Player player = playerInfo.getOnlinePlayer();
		if (
				Type.FULL.getUrl() == null
				|| Type.FULL.getHash() == null
				|| Type.LITE.getUrl() == null
				|| Type.LITE.getHash() == null
		) {
			PlayerUtils.kickPlayer(player, "Вы были кикнуты", "Сервер ещё не запущен");
			return;
		}
		if (playerInfo.getResourcePackType() != null) {
			if (playerInfo.getResourcePackType() == Type.FULL) {
				player.setResourcePack(Type.FULL.getUrl(), Type.FULL.getHash());
			} else if (playerInfo.getResourcePackType() == Type.LITE) {
				player.setResourcePack(Type.LITE.getUrl(), Type.LITE.getHash());
			} else {
				ChatUtils.sendWarning(player, Component.text("Вы зашли на сервер без ресурспака"));
			}
		} else {
			player.openInventory(ResourcePack.Menu.getInventory());
		}
	}

	public enum Type {
		FULL(null),
		LITE(null),
		NONE(null);

		private @Nullable ResourcePack resourcePack;

		Type(@Nullable ResourcePack resourcePack) {
			this.resourcePack = resourcePack;
		}

		public @Nullable String getHash() {
			return this.resourcePack == null ? null : this.resourcePack.hash;
		}

		public @Nullable String getUrl() {
			return this.resourcePack == null ? null : this.resourcePack.url;
		}

		/**
		 * @param name ResourcePack type name
		 * @return ResourcePackType by name
		 */
		public static @Nullable Type getResourcePackByString(@NotNull String name) {
			return switch (name) {
				case "FULL" -> FULL;
				case "LITE" -> LITE;
				case "NONE" -> NONE;
				default -> null;
			};
		}
	}

	public static class Menu {
		public static final Component NAME = Component.text("Выберите нужный текстурпак", NamedTextColor.DARK_GRAY);

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
					" - кастомные текстуры и модельки",
					" - переименуемые предметы",
					" - изменённая модель головы стива"
			));
			lite.setItemMeta(liteMeta);

			ItemStack full = new ItemStack(Material.NETHERITE_BLOCK);
			ItemMeta fullMeta = full.getItemMeta();
			fullMeta.displayName(Component.text(ChatColor.WHITE + "Полная версия"));
			fullMeta.lore(convertStringsToComponents(
					COLORLESS_DEFAULT_STYLE.color(NamedTextColor.GRAY),
					"Имеет в себе :",
					" - кастомные текстуры и модельки",
					" - переименуемые предметы",
					" - изменённая модель головы стива",
					" - анимированные текстуры",
					"   блоков/предметов",
					" - изменённые текстуры/модели",
					"   блоков/предметов/интерфейса",
					" - 3D модель фонаря",
					" - OF текстуры и модельки :",
					"   Небо",
					"   Стойка для брони",
					"   CIT предметы"
			));
			full.setItemMeta(fullMeta);

			Inventory inventory = Bukkit.createInventory(null, 9, NAME);
			inventory.setContents(new ItemStack[]{none, none, full, full, pick, full, full, lite, lite});
			return inventory;
		}
	}
}
