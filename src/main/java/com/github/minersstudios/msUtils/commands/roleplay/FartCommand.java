package com.github.minersstudios.msUtils.commands.roleplay;

import com.github.minersstudios.msDecor.enums.CustomDecorMaterial;
import com.github.minersstudios.msDecor.objects.CustomDecor;
import com.github.minersstudios.msUtils.Main;
import com.github.minersstudios.msUtils.classes.PlayerInfo;
import com.github.minersstudios.msUtils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import javax.annotation.Nonnull;
import java.util.Random;

public class FartCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
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
			new CustomDecor(location.getBlock(), player).setCustomDecor(CustomDecorMaterial.POOP, BlockFace.UP, null,
					Component.text("Какашка ")
							.append(playerInfo.getDefaultName())
							.style(Style.style(
									NamedTextColor.WHITE,
									TextDecoration.OBFUSCATED.withState(false),
									TextDecoration.BOLD.withState(false),
									TextDecoration.ITALIC.withState(false),
									TextDecoration.STRIKETHROUGH.withState(false),
									TextDecoration.UNDERLINED.withState(false)))
			);
		}
		if (args.length > 0) {
			return ChatUtils.sendRPEventMessage(player, Component.text(ChatUtils.extractMessage(args, 0)), Component.text(withPoop ? "пукнув с подливой" : "пукнув"), ChatUtils.RolePlayActionType.TODO);
		}
		return ChatUtils.sendRPEventMessage(player, Component.text(playerInfo.getPronouns().getFartMessage()).append(Component.text(withPoop ? " с подливой" : "")), ChatUtils.RolePlayActionType.ME);
	}
}
