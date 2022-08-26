package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.classes.Chat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigCache {
	public final YamlConfiguration yamlConfiguration;
	public final Map<String, Chat> chats = new HashMap<>();
	public final String
			full_hash,
			full_dropbox_url,
			full_yandex_disk_url,
			lite_hash,
			lite_dropbox_url,
			lite_yandex_disk_url;
	public final boolean
			enable_msdecor_hook,
			ignore_msdecor_version_check,
			enable_msblock_hook,
			ignore_msblock_version_check,
			enable_discordsrv_hook,
			ignore_discordsrv_version_check,
			enable_authme_hook,
			ignore_authme_version_check,
			ignore_protocollib_version_check;

	public final double local_chat_radius = 25.0d;

	public final String
			discord_global_channel_id = "",
			discord_local_channel_id = "";

	public final boolean
			send_global_messages_to_discord = true,
			send_local_messages_to_discord = true;

	public ConfigCache() {
		File configFile = new File(Main.getInstance().getDataFolder(), "config.yml");
		yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);

		loadChats();

		this.full_hash = yamlConfiguration.getString("resource-packs.full.hash");
		this.full_dropbox_url = yamlConfiguration.getString("resource-packs.full.dropbox-url");
		this.full_yandex_disk_url = yamlConfiguration.getString("resource-packs.full.yandex-disk-url");

		this.lite_hash = yamlConfiguration.getString("resource-packs.lite.hash");
		this.lite_dropbox_url = yamlConfiguration.getString("resource-packs.lite.dropbox-url");
		this.lite_yandex_disk_url = yamlConfiguration.getString("resource-packs.lite.yandex-disk-url");

		this.enable_msdecor_hook = yamlConfiguration.getBoolean("plugin-hooks.msdecor.enabled");
		this.ignore_msdecor_version_check = yamlConfiguration.getBoolean("plugin-hooks.msdecor.ignore-version-check");
		this.enable_msblock_hook = yamlConfiguration.getBoolean("plugin-hooks.msblock.enabled");
		this.ignore_msblock_version_check = yamlConfiguration.getBoolean("plugin-hooks.msblock.ignore-version-check");
		this.enable_discordsrv_hook = yamlConfiguration.getBoolean("plugin-hooks.discordsrv.enabled");
		this.ignore_discordsrv_version_check = yamlConfiguration.getBoolean("plugin-hooks.discordsrv.ignore-version-check");
		this.enable_authme_hook = yamlConfiguration.getBoolean("plugin-hooks.authme.enabled");
		this.ignore_authme_version_check = yamlConfiguration.getBoolean("plugin-hooks.authme.ignore-version-check");
		this.ignore_protocollib_version_check = yamlConfiguration.getBoolean("plugin-hooks.protocollib.ignore-version-check");
	}

	private void loadChats() {
		ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("chat");
		if (configurationSection == null) {
			ChatUtils.log(Level.SEVERE, "Chat section in configuration is not found!");
			Bukkit.getPluginManager().disablePlugin(Main.getInstance());
			return;
		}
		for (String chatName : configurationSection.getKeys(false)) {
			chats.put(chatName, new Chat(
					yamlConfiguration.getBoolean("chat." + chatName + ".enabled", false),
					yamlConfiguration.getDouble("chat." + chatName + ".radius", -1),
					yamlConfiguration.getStringList("chat." + chatName + ".permissions"),
					TextColor.fromHexString(yamlConfiguration.getString("chat." + chatName + ".colors.primary", "#FFFFFF")),
					TextColor.fromHexString(yamlConfiguration.getString("chat." + chatName + ".colors.secondary", "#FFFFFF")),
					yamlConfiguration.getBoolean("chat." + chatName + ".discord.enabled", false),
					yamlConfiguration.getStringList("chat." + chatName + ".discord.discord-channel-id"),
					yamlConfiguration.getString("chat." + chatName + ".symbol"),
					yamlConfiguration.getBoolean("chat." + chatName + ".enable-chat-symbols")
			));
		}
		if (!chats.containsKey("join")) {
			ChatUtils.log(Level.SEVERE, "Join chat section in configuration is not found!");
			Bukkit.getPluginManager().disablePlugin(Main.getInstance());
			return;
		}
		if (!chats.containsKey("quit")) {
			ChatUtils.log(Level.SEVERE, "Quit chat section in configuration is not found!");
			Bukkit.getPluginManager().disablePlugin(Main.getInstance());
			return;
		}
		if (!chats.containsKey("actions")) {
			ChatUtils.log(Level.SEVERE, "Actions chat section in configuration is not found!");
			Bukkit.getPluginManager().disablePlugin(Main.getInstance());
		}
	}

	@SuppressWarnings("unused")
	public static class Colors {
		public static final TextColor
				CHAT_COLOR_PRIMARY = TextColor.color(171, 164, 148),
				CHAT_COLOR_SECONDARY = TextColor.color(241, 240, 227),
				JOIN_MESSAGE_COLOR_PRIMARY = TextColor.color(255, 238, 147),
				JOIN_MESSAGE_COLOR_SECONDARY = TextColor.color(252, 245, 199),
				RP_MESSAGE_COLOR_PRIMARY = TextColor.color(255, 170, 0),
				RP_MESSAGE_COLOR_SECONDARY = TextColor.color(255, 195, 105);
	}

	@SuppressWarnings("unused")
	public static class Symbols {
		public static final Component
				GREEN_EXCLAMATION_MARK = Component.text(" ꀒ "),
				YELLOW_EXCLAMATION_MARK = Component.text(" ꀓ "),
				RED_EXCLAMATION_MARK = Component.text(" ꀑ "),
				SPEECH = Component.text(" ꀕ "),
				DISCORD = Component.text(" ꀔ "),
				PAINTABLE_BADGE = Component.text(" ꀢ ");
	}
}
