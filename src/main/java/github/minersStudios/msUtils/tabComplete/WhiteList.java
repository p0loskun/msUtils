package github.minersStudios.msUtils.tabComplete;

import github.minersStudios.msUtils.classes.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WhiteList implements TabCompleter {

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			completions.add("add");
			completions.add("remove");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
			for (OfflinePlayer offlinePlayer : Bukkit.getWhitelistedPlayers()) {
				PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
				if (playerInfo.hasPlayerDataFile())
					completions.add(String.valueOf(playerInfo.getID()));
				completions.add(offlinePlayer.getName());
			}
		}
		return completions;
	}
}
