package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand {

	public static boolean runCommand(@NotNull CommandSender sender) {
		long time = System.currentTimeMillis();
		MSUtils.getConfigCache().playerAnomalyActionMap.clear();
		MSUtils.reloadConfigs();
		if (MSUtils.getInstance().isEnabled()) {
			return ChatUtils.sendFine(sender, Component.text("Плагин был успешно перезагружён за " + (System.currentTimeMillis() - time) + "ms"));
		}
		return ChatUtils.sendError(sender, Component.text("Плагин был перезагружён неудачно"));
	}
}
