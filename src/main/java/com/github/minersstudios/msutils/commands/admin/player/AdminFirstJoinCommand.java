package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.DateUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static net.kyori.adventure.text.Component.text;

public class AdminFirstJoinCommand {

	public static boolean runCommand(
			@NotNull CommandSender sender,
			@NotNull PlayerInfo playerInfo
	) {
		ChatUtils.sendFine(sender,
				text("Впервые игрок : ")
				.append(playerInfo.getGrayIDGreenName())
				.appendNewline()
				.append(text("    Зашёл на сервер в : "))
				.append(text(DateUtils.getSenderDate(new Date(playerInfo.getPlayerFile().getFirstJoin()), sender)))
		);
		return true;
	}
}
