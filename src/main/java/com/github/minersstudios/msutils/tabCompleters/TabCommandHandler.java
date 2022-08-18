package com.github.minersstudios.msutils.tabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TabCommandHandler implements TabCompleter {

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			completions.add("reload");
		}
		return completions;
	}
}
