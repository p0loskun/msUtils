package com.github.minersstudios.msutils.tabcompleters;

import com.github.minersstudios.mscore.utils.CommandUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.IDUtils;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
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
	public @Nullable List<String> onTabComplete(
			@NotNull CommandSender sender,
			@NotNull Command command,
			@NotNull String label,
			String @NotNull ... args
	) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
				String nickname = offlinePlayer.getName();
				if (nickname == null) continue;
				int id = IDUtils.getID(offlinePlayer.getUniqueId(), false, false);
				switch (command.getName()) {
					case "mute" -> {
						PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), nickname);
						if (playerInfo.isMuted()) continue;
					}
					case "unmute" -> {
						PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), nickname);
						if (!playerInfo.isMuted()) continue;
					}
					case "ban" -> {
						PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), nickname);
						if (playerInfo.isBanned()) continue;
					}
				}
				if (id != -1) {
					completions.add(String.valueOf(id));
				}
				if (offlinePlayer.hasPlayedBefore()) {
					completions.add(nickname);
				}
			}
		}
		if (args.length == 2) {
			switch (command.getName()) {
				case "mute", "ban" -> completions.addAll(CommandUtils.getTimeSuggestions(args[1]));
			}
		}
		return completions;
	}
}
