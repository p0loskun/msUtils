package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand {

	public static boolean runCommand(@NotNull CommandSender sender) {
		Main.reloadConfigs();
		return ChatUtils.sendFine(sender, Component.text("Плагин был перезагружён"));
	}
}
