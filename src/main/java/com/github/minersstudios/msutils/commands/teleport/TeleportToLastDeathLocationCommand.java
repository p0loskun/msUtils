package com.github.minersstudios.msutils.commands.teleport;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerID;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.tabcompleters.AllLocalPlayers;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@MSCommand(
		command = "teleporttolastdeathlocation",
		aliases = {"teleporttolastdeathloc", "tptolastdeathlocation", "tptolastdeathloc", "tptolastdeath"},
		usage = " ꀑ §cИспользуй: /<command> [ID/Nickname]",
		description = "Телепортирует игрока на его последнее место смерти",
		permission = "msutils.teleporttolastdeathlocation",
		permissionDefault = PermissionDefault.OP
)
public class TeleportToLastDeathLocationCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length == 0) return false;
		if (args[0].matches("-?\\d+")) {
			OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
				return true;
			}
			teleportToLastDeathLocation(sender, offlinePlayer);
			return true;
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
				return true;
			}
			teleportToLastDeathLocation(sender, offlinePlayer);
			return true;
		}
		ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		return new AllLocalPlayers().onTabComplete(sender, command, label, args);
	}

	private static void teleportToLastDeathLocation(@NotNull CommandSender sender, @NotNull OfflinePlayer offlinePlayer) {
		if (!offlinePlayer.hasPlayedBefore() || offlinePlayer.getName() == null) {
			ChatUtils.sendWarning(sender, Component.text("Данный игрок ещё ни разу не играл на сервере"));
			return;
		}
		PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
		Location lastDeathLocation = offlinePlayer.getLastDeathLocation();
		if (offlinePlayer.getPlayer() == null) {
			ChatUtils.sendWarning(sender,
					Component.text("Игрок : \"")
					.append(playerInfo.createGrayIDGreenName())
					.append(Component.text(" ("))
					.append(Component.text(offlinePlayer.getName()))
					.append(Component.text(")\" не в сети!"))
			);
			return;
		}
		if (lastDeathLocation == null) {
			ChatUtils.sendWarning(sender,
					Component.text("Игрок : \"")
					.append(playerInfo.createGrayIDGreenName())
					.append(Component.text(" ("))
					.append(Component.text(offlinePlayer.getName()))
					.append(Component.text(")\" не имеет последней точки смерти!"))
			);
			return;
		}
		offlinePlayer.getPlayer().teleportAsync(lastDeathLocation.add(0.5d, 0.0d, 0.5d), PlayerTeleportEvent.TeleportCause.PLUGIN);
		ChatUtils.sendFine(sender,
				Component.text("Игрок : \"")
				.append(playerInfo.createGrayIDGreenName())
				.append(Component.text(" ("))
				.append(Component.text(offlinePlayer.getName()))
				.append(Component.text(")\" был телепортирован на последние координаты смерти"))
		);
	}
}
