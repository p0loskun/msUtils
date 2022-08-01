package com.github.MinersStudios.msUtils.tabCompleters;

import com.github.MinersStudios.msUtils.classes.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AllLocalPlayers implements TabCompleter {

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
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
		return completions;
	}
}
