package com.github.minersstudios.msutils.commands.roleplay;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@MSCommand(
		command = "sit",
		aliases = {"s"},
		usage = " ꀑ §cИспользуй: /<command> [речь]",
		description = "Сядь на картаны и порви жопу"
)
public class SitCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
			return true;
		}
		if (!PlayerUtils.isOnline(player)) return true;
		if (!player.getLocation().subtract(0.0d, 0.2d, 0.0d).getBlock().getType().isSolid()) {
			ChatUtils.sendWarning(player, Component.text("Сидеть в воздухе нельзя!"));
			return true;
		}
		PlayerUtils.setSitting(player, MSUtils.getConfigCache().seats.containsKey(player) ? null : player.getLocation(), args.length > 0 ? args : null);
		return true;
	}
}
