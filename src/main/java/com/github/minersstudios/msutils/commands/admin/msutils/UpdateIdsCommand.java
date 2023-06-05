package com.github.minersstudios.msutils.commands.admin.msutils;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.config.ConfigCache;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class UpdateIdsCommand {

	public static void runCommand(@NotNull CommandSender sender) {
		ConfigCache configCache = MSUtils.getConfigCache();
		configCache.idMap.clear();
		configCache.idsYaml = YamlConfiguration.loadConfiguration(configCache.idsFile);
		for (Map.Entry<String, Object> entry : configCache.idsYaml.getValues(true).entrySet()) {
			configCache.idMap.put(UUID.fromString(entry.getKey()), (Integer) entry.getValue());
		}
		ChatUtils.sendFine(sender, "Список айди был успешно перезагружен");
	}
}
