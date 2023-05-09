package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.ResourcePack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@MSCommand(
		command = "resourcepack",
		aliases = {"texturepack", "rp"},
		usage = " ꀑ §cИспользуй: /<command>",
		description = "Открывает меню с ресурспаками"
)
public class ResourcePackCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
			return true;
		}
		boolean resourcePack = ResourcePack.Menu.open(player);
		if (!resourcePack) {
			ChatUtils.sendError(sender, Component.text("Кажется, что-то пошло не так..."));
		}
		return true;
	}
}
