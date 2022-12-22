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

public class WhiteList implements TabCompleter {

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			completions.add("add");
			completions.add("remove");
			completions.add("reload");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
			for (OfflinePlayer offlinePlayer : Bukkit.getWhitelistedPlayers()) {
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
}
