package com.github.minersstudios.msutils.commands.teleport;

import com.github.minersstudios.msutils.player.PlayerID;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class WorldTeleportCommand implements CommandExecutor {
	private static final String coordinatesRegex = "^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$";

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length < 2) return false;
		if (args[0].matches("[0-99]+")) {
			OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null) {
				return ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
			}
			return teleportToWorld(sender, offlinePlayer, args);
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				return ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
			}
			return teleportToWorld(sender, offlinePlayer, args);
		}
		return ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
	}

	private static boolean teleportToWorld(@NotNull CommandSender sender, @NotNull OfflinePlayer offlinePlayer, String @NotNull ... args) {
		if (!offlinePlayer.hasPlayedBefore()) {
			return ChatUtils.sendWarning(sender, Component.text("Данный игрок ещё ни разу не играл на сервере"));
		}
		PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
		if (offlinePlayer.getPlayer() == null) {
			return ChatUtils.sendWarning(sender,
					Component.text("Игрок : \"")
							.append(playerInfo.getGrayIDGoldName())
							.append(Component.text("\" не в сети!"))
			);
		}
		World world = Bukkit.getWorld(args[1]);
		if (world == null) {
			return ChatUtils.sendWarning(sender, Component.text("Такого мира не существует!"));
		}
		Location spawnLoc = world.getSpawnLocation();
		double
				x = spawnLoc.getX(),
				y = spawnLoc.getY(),
				z = spawnLoc.getZ();
		if (args.length > 2) {
			if (args.length != 5 || !args[2].matches(coordinatesRegex) || !args[3].matches(coordinatesRegex) || !args[4].matches(coordinatesRegex)) return false;
			x = Double.parseDouble(args[2]);
			y = Double.parseDouble(args[3]);
			z = Double.parseDouble(args[4]);
			if (x > 29999984 || z > 29999984) {
				return ChatUtils.sendWarning(sender, Component.text("Указаны слишком большие координаты!"));
			}
		}
		offlinePlayer.getPlayer().teleportAsync(new Location(world, x, y, z), PlayerTeleportEvent.TeleportCause.PLUGIN);
		return ChatUtils.sendFine(sender,
				Component.text("Игрок : \"")
						.append(playerInfo.getGrayIDGreenName())
						.append(Component.text(" ("))
						.append(Component.text(args[0]))
						.append(Component.text(")\" был телепортирован :"))
						.append(Component.text("\n    - Мир : "))
						.append(Component.text(world.getName()))
						.append(Component.text("\n    - X : "))
						.append(Component.text(x))
						.append(Component.text("\n    - Y : "))
						.append(Component.text(y))
						.append(Component.text("\n    - Z : "))
						.append(Component.text(z))
		);
	}
}
