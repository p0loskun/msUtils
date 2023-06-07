package com.github.minersstudios.msutils.commands.admin.msutils;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class ReloadCommand {

	public static void runCommand(@NotNull CommandSender sender) {
		long time = System.currentTimeMillis();

		MSUtils.getConfigCache().playerAnomalyActionMap.clear();
		MSUtils.reloadConfigs();

		if (MSUtils.getInstance().isEnabled()) {
			ChatUtils.sendFine(
					sender,
					text("Плагин был успешно перезагружен за ")
					.append(text(System.currentTimeMillis() - time))
					.append(text("ms"))
			);
			return;
		}

		ChatUtils.sendError(sender, "Плагин был перезагружен неудачно");
	}
}
