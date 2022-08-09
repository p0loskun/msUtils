package com.github.minersstudios.msUtils.commands.other;

import com.github.minersstudios.msUtils.Main;
import com.github.minersstudios.msUtils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class ReloadCommand {

	public static boolean runCommand(@Nonnull CommandSender sender) {
		Main.reloadConfigs();
		return ChatUtils.sendFine(sender, Component.text("Плагин был перезагружён"));
	}
}
