package github.minersStudios.msUtils.tabComplete;

import github.minersStudios.msUtils.classes.PlayerID;
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
		if(args.length == 1) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				completions.add(String.valueOf(new PlayerID().getPlayerID(player.getUniqueId())));
				completions.add(player.getName());
			}
		}
		return completions;
	}
}
