package com.github.minersstudios.msutils.commands.admin.msutils;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
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
		permissionParentKeys = {
				"msutils.player.*",
				"msutils.ban",
				"msutils.mute",
				"msutils.kick",
				"msutils.maplocation",
				"msutils.whitelist",
				"msutils.teleporttolastdeathlocation",
				"msutils.worldteleport"
		},
		permissionParentValues = {
				true,
				true,
				true,
				true,
				true,
				true,
				true,
				true
		}
)
public class MSUtilsCommandHandler implements MSCommandExecutor {

	@Override
	public boolean onCommand(
			@NotNull CommandSender sender, 
			@NotNull Command command, 
			@NotNull String label, 
			String @NotNull ... args
	) {
		if (args.length > 0) {
			switch (args[0].toLowerCase(Locale.ENGLISH)) {
				case "reload" -> ReloadCommand.runCommand(sender);
				case "updateids" -> UpdateIdsCommand.runCommand(sender);
				default -> {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length == 1) {
			return List.of(
					"reload",
					"updateids"
			);
		}
		return new ArrayList<>();
	}
}
