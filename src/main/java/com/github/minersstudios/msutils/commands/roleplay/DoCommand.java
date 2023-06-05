package com.github.minersstudios.msutils.commands.roleplay;

import com.github.minersstudios.mscore.command.MSCommand;
import com.github.minersstudios.mscore.command.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.minersstudios.msutils.utils.MessageUtils.RolePlayActionType.DO;
import static com.github.minersstudios.msutils.utils.MessageUtils.sendRPEventMessage;
import static net.kyori.adventure.text.Component.text;

@MSCommand(
		command = "do",
		usage = " ꀑ §cИспользуй: /<command> [действие]",
		description = "Описывает состояние вашего персонажа и объектов вокруг вас"
)
public class DoCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(
			@NotNull CommandSender sender, 
			@NotNull Command command, 
			@NotNull String label, 
			String @NotNull ... args
	) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, "Только игрок может использовать эту команду!");
			return true;
		}
		PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(player);
		if (!playerInfo.isOnline()) return true;
		if (args.length == 0) return false;
		if (playerInfo.getPlayerFile().isMuted()) {
			ChatUtils.sendWarning(player, "Вы замьючены");
			return true;
		}
		sendRPEventMessage(player, text(ChatUtils.extractMessage(args, 0)), DO);
		return true;
	}
	@Override
	public @Nullable CommandNode<?> getCommandNode() {
		return LiteralArgumentBuilder.literal("do")
				.then(RequiredArgumentBuilder.argument("действие", StringArgumentType.greedyString()))
				.build();
	}
}
