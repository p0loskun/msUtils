package com.github.minersstudios.msutils.commands.roleplay;

import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		}
		World world = player.getWorld();
		Location location = player.getLocation();
		if (player.getWorld() == Main.getWorldDark() || !Main.getAuthMeApi().isAuthenticated(player)) return true;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted()) {
			return ChatUtils.sendWarning(player, Component.text("Вы замьючены"));
		}
		world.spawnEntity(
				location.toVector().add(location.getDirection().multiply(0.8d)).toLocation(world).add(0.0d, 1.0d, 0.0d),
				EntityType.LLAMA_SPIT
		).setVelocity(player.getEyeLocation().getDirection().multiply(1));
		world.playSound(location, Sound.ENTITY_LLAMA_SPIT, SoundCategory.PLAYERS, 1.0f, 1.0f);
		if (args.length > 0) {
			return ChatUtils.sendRPEventMessage(player, Component.text(ChatUtils.extractMessage(args, 0)), Component.text("плюнув"), ChatUtils.RolePlayActionType.TODO);
		}
		return ChatUtils.sendRPEventMessage(player, Component.text(playerInfo.getPronouns().getSpitMessage()), ChatUtils.RolePlayActionType.ME);
	}
}
