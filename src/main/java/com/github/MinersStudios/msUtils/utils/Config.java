package com.github.MinersStudios.msUtils.utils;

import com.github.MinersStudios.msUtils.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {
	public final String
			discord_global_channel_id,
			discord_local_channel_id,
			full_dropbox_url,
			full_yandex_disk_url,
			lite_dropbox_url,
			lite_yandex_disk_url;
	public final double local_chat_radius;

	public Config() {
		File dataFile = new File(Main.getInstance().getDataFolder(), "config.yml");
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(dataFile);

		this.local_chat_radius = yamlConfiguration.getDouble("chat.local.radius");

		this.discord_global_channel_id = yamlConfiguration.getString("chat.global.discord-channel-id");
		this.discord_local_channel_id = yamlConfiguration.getString("chat.local.discord-channel-id");

		this.full_dropbox_url = yamlConfiguration.getString("resource-pack.full-dropbox-url", "https://dropbox.com/");
		this.full_yandex_disk_url = yamlConfiguration.getString("resource-pack.full-yandex-disk-url", "https://disk.yandex.ru/");
		this.lite_dropbox_url = yamlConfiguration.getString("resource-pack.lite-dropbox-url", "https://dropbox.com/");
		this.lite_yandex_disk_url = yamlConfiguration.getString("resource-pack.lite-yandex-disk-url", "https://disk.yandex.ru/");
	}

	@SuppressWarnings("unused")
	public static class Colors {
		public static final TextColor
				chatColorPrimary = TextColor.color(171, 164, 148),
				chatColorSecondary = TextColor.color(241, 240, 227),
				joinMessageColorPrimary = TextColor.color(255, 238, 147),
				joinMessageColorSecondary = TextColor.color(252, 245, 199),
				rpMessageMessageColorPrimary = TextColor.color(255, 170, 0),
				rpMessageMessageColorSecondary = TextColor.color(255, 195, 105);
	}

	@SuppressWarnings("unused")
	public static class Symbols {
		public static final Component
				greenExclamationMark = Component.text(" ꀒ "),
				yellowExclamationMark = Component.text(" ꀓ "),
				redExclamationMark = Component.text(" ꀑ "),
				speech = Component.text(" ꀕ "),
				discord = Component.text(" ꀔ "),
				paintableBadge = Component.text(ChatColor.WHITE + "ꀢ ");
	}
}
