package com.github.minersstudios.msutils.commands;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.msutils.commands.admin.ReloadCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@MSCommand(
		command = "msutils",
		usage = " ꀑ §cИспользуй: /<command> [параметры]",
		description = "Прочие команды",
		permission = "msutils.*",
		permissionDefault = PermissionDefault.OP,
		permissionParentKeys = {"msUtils.info", "msUtils.ban", "msUtils.mute", "msUtils.kick", "msUtils.maplocation", "msUtils.whitelist", "msUtils.teleporttolastdeathlocation", "msUtils.worldteleport"},
		permissionParentValues = {true, true, true, true, true, true, true, true}
)
public class CommandHandler implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length > 0) {
			String utilsCommand = args[0].toLowerCase(Locale.ROOT);
			if ("reload".equals(utilsCommand)) {
				ReloadCommand.runCommand(sender);
				return true;
			}
		}
		return false;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			completions.add("reload");
		}
		return completions;
	}
}
