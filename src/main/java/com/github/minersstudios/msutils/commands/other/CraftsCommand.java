package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.inventory.CraftsMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@MSCommand(
		command = "crafts",
		aliases = {"recipes"},
		usage = " ꀑ §cИспользуй: /<command>",
		description = "Открывает меню с крафтами кастомных предметов/декора/блоков"
)
public class CraftsCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
			return true;
		}
		boolean crafts = CraftsMenu.open(player);
		if (!crafts) {
			ChatUtils.sendError(sender, Component.text("Кажется, что-то пошло не так..."));
		}
		return true;
	}
}
