package com.github.minersstudios.msutils.commands.roleplay;

import com.github.minersstudios.msdecor.customdecor.CustomDecor;
import com.github.minersstudios.msdecor.utils.CustomDecorUtils;
import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FartCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		}
		if (player.getWorld() == Main.getWorldDark() || !Main.getAuthMeApi().isAuthenticated(player)) return true;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted()) {
			return ChatUtils.sendWarning(player, Component.text("Вы замьючены"));
		}
		Location location = player.getLocation();
		boolean withPoop = new Random().nextInt(10) == 0 && location.clone().subtract(0.0d, 0.5d, 0.0d).getBlock().getType().isSolid();
		for (Entity nearbyEntity : player.getWorld().getNearbyEntities(location.getBlock().getLocation().add(0.5d, 0.5d, 0.5d), 0.5d, 0.5d, 0.5d)) {
			if (nearbyEntity.getType() != EntityType.DROPPED_ITEM && nearbyEntity.getType() != EntityType.PLAYER) {
				withPoop = false;
				break;
			}
		}
		player.getWorld().playSound(location.add(0, 0.4, 0), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
		player.getWorld().spawnParticle(Particle.REDSTONE, location, 15, 0.0D, 0.0D, 0.0D, 0.5D, new Particle.DustOptions(Color.fromBGR(33, 54, 75), 10));
		if (withPoop) {
			new CustomDecor(location.getBlock(), player, CustomDecorUtils.CUSTOM_DECORS.get("poop"))
					.setCustomDecor(
							BlockFace.UP,
							null,
							ChatUtils.createDefaultStyledName("Какашка " + ChatUtils.convertComponentToString(playerInfo.getDefaultName()))
			);
		}
		if (args.length > 0) {
			return ChatUtils.sendRPEventMessage(player, Component.text(ChatUtils.extractMessage(args, 0)), Component.text(withPoop ? "пукнув с подливой" : "пукнув"), ChatUtils.RolePlayActionType.TODO);
		}
		return ChatUtils.sendRPEventMessage(player, Component.text(playerInfo.getPronouns().getFartMessage()).append(Component.text(withPoop ? " с подливой" : "")), ChatUtils.RolePlayActionType.ME);
	}
}
