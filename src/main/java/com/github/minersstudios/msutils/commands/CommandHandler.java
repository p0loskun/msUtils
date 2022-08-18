package com.github.minersstudios.msutils.commands;

import com.github.minersstudios.msutils.commands.other.ReloadCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.Locale;

public class CommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (args.length > 0) {
			String utilsCommand = args[0].toLowerCase(Locale.ROOT);
			if ("reload".equals(utilsCommand)) {
				return ReloadCommand.runCommand(sender);
			}
		}
		return false;
	}
}
