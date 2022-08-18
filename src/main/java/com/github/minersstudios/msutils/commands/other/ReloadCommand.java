package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class ReloadCommand {

	public static boolean runCommand(@Nonnull CommandSender sender) {
		long time = System.currentTimeMillis();
		HandlerList.unregisterAll(Main.getInstance());
		Main.load(Main.getInstance());
		if (Main.getInstance().isEnabled()) {
			return ChatUtils.sendFine(sender, Component.text("Плагин был успешно перезагружён за " + (System.currentTimeMillis() - time) + "ms"));
		}
		return ChatUtils.sendError(sender, Component.text("Плагин был перезагружён неудачно"));
	}
}
