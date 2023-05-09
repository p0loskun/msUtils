package com.github.minersstudios.msutils.commands.teleport;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerID;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@MSCommand(
		command = "worldteleport",
		aliases = {"worldteleport", "worldtp", "wtp", "teleportworld", "tpworld", "tpw"},
		usage = " ꀑ §cИспользуй: /<command> [ID/Nickname] [world name] [x] [y] [z]",
		description = "Телепортирует игрока на координаты в указанном мире, если координаты не указаны, телепортирует на точку спавна данного мира",
		permission = "msutils.worldteleport",
		permissionDefault = PermissionDefault.OP
)
public class WorldTeleportCommand implements MSCommandExecutor {
	private static final String coordinatesRegex = "^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$";

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length < 2) return false;
		if (args[0].matches("-?\\d+")) {
			OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
				return true;
			}
			return teleportToWorld(sender, offlinePlayer, args);
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
				return true;
			}
			return teleportToWorld(sender, offlinePlayer, args);
		}
		ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
				int id = playerInfo.getID(false, false);
				if (id != -1) {
					completions.add(String.valueOf(id));
				}
				completions.add(player.getName());
			}
		}
		if (args.length == 2) {
			for (World world : Bukkit.getWorlds()) {
				if (world != MSUtils.getWorldDark()) {
					completions.add(world.getName());
				}
			}
		}
		return completions;
	}

	private static boolean teleportToWorld(@NotNull CommandSender sender, @NotNull OfflinePlayer offlinePlayer, String @NotNull ... args) {
		if (!offlinePlayer.hasPlayedBefore()) {
			ChatUtils.sendWarning(sender, Component.text("Данный игрок ещё ни разу не играл на сервере"));
			return true;
		}
		PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
		if (offlinePlayer.getPlayer() == null) {
			ChatUtils.sendWarning(sender,
					Component.text("Игрок : \"")
					.append(playerInfo.getGrayIDGoldName())
					.append(Component.text("\" не в сети!"))
			);
			return true;
		}
		World world = Bukkit.getWorld(args[1]);
		if (world == null) {
			ChatUtils.sendWarning(sender, Component.text("Такого мира не существует!"));
			return true;
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
				ChatUtils.sendWarning(sender, Component.text("Указаны слишком большие координаты!"));
				return true;
			}
		}
		offlinePlayer.getPlayer().teleportAsync(new Location(world, x, y, z), PlayerTeleportEvent.TeleportCause.PLUGIN);
		ChatUtils.sendFine(sender,
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
		return true;
	}
}
