package com.github.minersstudios.msutils.commands.admin.msutils;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.config.ConfigCache;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

public class UpdateIdsCommand {

	public static void runCommand(@NotNull CommandSender sender) {
		long time = System.currentTimeMillis();
		ConfigCache configCache = MSUtils.getConfigCache();
		configCache.idMap.clear();
		configCache.idsYaml = YamlConfiguration.loadConfiguration(configCache.idsFile);
		for (Map.Entry<String, Object> entry : configCache.idsYaml.getValues(true).entrySet()) {
			configCache.idMap.put(UUID.fromString(entry.getKey()), (Integer) entry.getValue());
		}
		for (PlayerInfo playerInfo : MSPlayerUtils.getPlayerMap().values()) {
			playerInfo.initNames();
		}
		ChatUtils.sendFine(
				sender,
				text("Список айди был успешно перезагружен за ")
						.append(text(System.currentTimeMillis() - time))
						.append(text("ms"))
		);
	}
}
