package com.github.minersstudios.msutils.commands.roleplay;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

import static com.github.minersstudios.msutils.utils.ChatUtils.RolePlayActionType.ME;
import static com.github.minersstudios.msutils.utils.ChatUtils.sendRPEventMessage;

@MSCommand(command = "try")
public class TryCommand implements MSCommandExecutor {
	private final SecureRandom random = new SecureRandom();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		}
		if (!PlayerUtils.isOnline(player)) return true;
		if (args.length == 0) return false;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted()) {
			return ChatUtils.sendWarning(player, Component.text("Вы замьючены"));
		}
		return sendRPEventMessage(player,
				Component.text(ChatUtils.extractMessage(args, 0))
				.append(Component.space())
				.append(new Component[]{
						Component.text("Успешно", NamedTextColor.GREEN),
						Component.text("Неуспешно", NamedTextColor.RED)
				}[this.random.nextInt(2)]),
				ME
		);
	}
}
