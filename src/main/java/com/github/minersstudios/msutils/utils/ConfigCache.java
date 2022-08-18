package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.msutils.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigCache {
	public final String
			discord_global_channel_id,
			discord_local_channel_id,
			full_dropbox_url,
			full_yandex_disk_url,
			lite_dropbox_url,
			lite_yandex_disk_url,
			full_hash,
			lite_hash;
	public final double local_chat_radius;
	public final boolean
			send_global_messages_to_discord,
			send_local_messages_to_discord,
			enable_msdecor_hook,
			enable_msblock_hook,
			enable_discordsrv_hook,
			enable_authme_hook;

	public ConfigCache() {
		File dataFile = new File(Main.getInstance().getDataFolder(), "config.yml");
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(dataFile);
		this.local_chat_radius = yamlConfiguration.getDouble("chat.local.radius");
		this.discord_global_channel_id = yamlConfiguration.getString("chat.global.discord-channel-id");
		this.send_global_messages_to_discord = yamlConfiguration.getBoolean("chat.global.send-messages-to-discord");
		this.discord_local_channel_id = yamlConfiguration.getString("chat.local.discord-channel-id");
		this.send_local_messages_to_discord = yamlConfiguration.getBoolean("chat.local.send-messages-to-discord");
		this.full_hash = yamlConfiguration.getString("resource-pack.full.hash");
		this.full_dropbox_url = yamlConfiguration.getString("resource-pack.full.dropbox-url");
		this.full_yandex_disk_url = yamlConfiguration.getString("resource-pack.full.yandex-disk-url");
		this.lite_hash = yamlConfiguration.getString("resource-pack.lite.hash");
		this.lite_dropbox_url = yamlConfiguration.getString("resource-pack.lite.dropbox-url");
		this.lite_yandex_disk_url = yamlConfiguration.getString("resource-pack.lite.yandex-disk-url");
		this.enable_msdecor_hook = yamlConfiguration.getBoolean("plugin-hooks.msdecor.enable");
		this.enable_msblock_hook = yamlConfiguration.getBoolean("plugin-hooks.msblock.enable");
		this.enable_discordsrv_hook = yamlConfiguration.getBoolean("plugin-hooks.discordsrv.enable");
		this.enable_authme_hook = yamlConfiguration.getBoolean("plugin-hooks.authme.enable");
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
				PAINTABLE_BADGE = Component.text(ChatColor.WHITE + "ꀢ ");
	}
}
