package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UpdatePlayerCommand {

	public static boolean runCommand(@NotNull CommandSender sender, @NotNull PlayerInfo playerInfo) {
		playerInfo.update();
		ChatUtils.sendFine(sender, "Данные игрока были успешно обновлены");
		return true;
	}
}
