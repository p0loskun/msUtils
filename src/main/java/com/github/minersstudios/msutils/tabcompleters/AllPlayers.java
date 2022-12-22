package com.github.minersstudios.msutils.tabcompleters;

import com.github.minersstudios.msutils.player.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AllPlayers implements TabCompleter {

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			if (command.getName().equals("unban")) {
				for (OfflinePlayer offlinePlayer : Bukkit.getBannedPlayers()) {
					if (offlinePlayer != null) {
						PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
						int id = playerInfo.getID(false, false);
						if (id != -1) {
							completions.add(String.valueOf(id));
						}
						completions.add(offlinePlayer.getName());
					}
				}
				return completions;
			}
			for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
				PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
				int id = playerInfo.getID(false, false);
				switch (command.getName()) {
					case "mute" -> {
						if (playerInfo.isMuted()) break;
						if (id != -1) {
							completions.add(String.valueOf(id));
						}
						if (playerInfo.hasPlayerDataFile()) {
							completions.add(offlinePlayer.getName());
						}
					}
					case "unmute" -> {
						if (!playerInfo.isMuted()) break;
						if (id != -1) {
							completions.add(String.valueOf(id));
						}
						if (playerInfo.hasPlayerDataFile()) {
							completions.add(offlinePlayer.getName());
						}
					}
					case "ban" -> {
						if (playerInfo.isBanned()) break;
						if (id != -1) {
							completions.add(String.valueOf(id));
						}
						completions.add(offlinePlayer.getName());
					}
					default -> {
						if (id != -1) {
							completions.add(String.valueOf(id));
						}
						if (playerInfo.hasPlayerDataFile()) {
							completions.add(offlinePlayer.getName());
						}
					}
				}
			}
		}
		return completions;
	}
}
