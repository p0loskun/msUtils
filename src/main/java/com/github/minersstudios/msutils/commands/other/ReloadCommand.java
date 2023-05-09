package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand {

	public static void runCommand(@NotNull CommandSender sender) {
		long time = System.currentTimeMillis();
		MSUtils.getConfigCache().playerAnomalyActionMap.clear();
		MSUtils.reloadConfigs();
		if (MSUtils.getInstance().isEnabled()) {
			ChatUtils.sendFine(sender, Component.text("Плагин был успешно перезагружен за " + (System.currentTimeMillis() - time) + "ms"));
			return;
		}
		ChatUtils.sendError(sender, Component.text("Плагин был перезагружен неудачно"));
	}
}
