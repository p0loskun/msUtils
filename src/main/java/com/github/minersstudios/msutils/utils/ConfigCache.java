package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.msutils.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class ConfigCache {
	public final String
			discordGlobalChannelId,
			discordLocalChannelId,
			fullDropboxUrl,
			fullYandexDiskUrl,
			liteDropboxUrl,
			liteYandexDiskUrl,
			fullHash,
			liteHash;
	public final double local_chat_radius;

	public ConfigCache() {
		File dataFile = new File(Main.getInstance().getDataFolder(), "config.yml");
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(dataFile);
		this.local_chat_radius = yamlConfiguration.getDouble("chat.local.radius");
		this.discordGlobalChannelId = yamlConfiguration.getString("chat.global.discord-channel-id");
		this.discordLocalChannelId = yamlConfiguration.getString("chat.local.discord-channel-id");
		this.fullHash = yamlConfiguration.getString("resource-pack.full.hash");
		this.fullDropboxUrl = yamlConfiguration.getString("resource-pack.full.dropbox-url");
		this.fullYandexDiskUrl = yamlConfiguration.getString("resource-pack.full.yandex-disk-url");
		this.liteHash = yamlConfiguration.getString("resource-pack.lite.hash");
		this.liteDropboxUrl = yamlConfiguration.getString("resource-pack.lite.dropbox-url");
		this.liteYandexDiskUrl = yamlConfiguration.getString("resource-pack.lite.yandex-disk-url");
	}
}
