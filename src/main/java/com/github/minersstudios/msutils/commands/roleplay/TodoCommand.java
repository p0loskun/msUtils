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

import static com.github.minersstudios.msutils.utils.ChatUtils.RolePlayActionType.TODO;
import static com.github.minersstudios.msutils.utils.ChatUtils.sendRPEventMessage;

@MSCommand(
		command = "todo",
		usage = " ꀑ §cИспользуй: /<command> [речь] * [действие]",
		description = "Описывает действие и речь в чате"
)
public class TodoCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
			return true;
		}
		if (!PlayerUtils.isOnline(player)) return true;
		String message = ChatUtils.extractMessage(args, 0);
		if (args.length < 3 || !message.contains("*")) return false;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted()) {
			ChatUtils.sendWarning(player, Component.text("Вы замьючены"));
			return true;
		}
		String action = message.substring(message.indexOf('*') + 1).trim(),
				speech = message.substring(0 , message.indexOf('*')).trim();
		if (action.isEmpty() || speech.isEmpty()) return false;
		sendRPEventMessage(player, Component.text(speech), Component.text(action), TODO);
		return true;
	}
}
