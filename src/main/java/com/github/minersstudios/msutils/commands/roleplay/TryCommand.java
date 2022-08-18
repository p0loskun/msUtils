package com.github.minersstudios.msutils.commands.roleplay;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.classes.PlayerInfo;
import com.github.minersstudios.msutils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.security.SecureRandom;

public class TryCommand implements CommandExecutor {
	private final SecureRandom random = new SecureRandom();

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		}
		if (player.getWorld() == Main.getWorldDark() || !Main.getAuthMeApi().isAuthenticated(player)) return true;
		if (args.length == 0) return false;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted()) {
			return ChatUtils.sendWarning(player, Component.text("Вы замьючены"));
		}
		return ChatUtils.sendRPEventMessage(player,
				Component.text(ChatUtils.extractMessage(0, args))
						.append(Component.text(" "))
						.append(new Component[]{
								Component.text("Успешно", NamedTextColor.GREEN),
								Component.text("Неуспешно", NamedTextColor.RED)
						}[random.nextInt(2)]),
				ChatUtils.RolePlayActionType.ME
		);
	}
}
