package com.github.minersstudios.msutils.commands.roleplay;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.github.minersstudios.msutils.utils.ChatUtils.RolePlayActionType.ME;
import static com.github.minersstudios.msutils.utils.ChatUtils.sendRPEventMessage;

@MSCommand(
		command = "me",
		usage = " ꀑ §cИспользуй: /<command> [действие]",
		description = "Описывает, что делает ваш персонаж"
)
public class MeCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
			return true;
		}
		if (!PlayerUtils.isOnline(player)) return true;
		if (args.length == 0) return false;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted()) {
			ChatUtils.sendWarning(player, Component.text("Вы замьючены"));
			return true;
		}
		sendRPEventMessage(player, Component.text(ChatUtils.extractMessage(args, 0)), ME);
		return true;
	}
}
