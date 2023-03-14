package com.github.minersstudios.msutils.commands.roleplay;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.BlockUtils;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.MSDecorUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

import static com.github.minersstudios.msutils.utils.ChatUtils.RolePlayActionType.ME;
import static com.github.minersstudios.msutils.utils.ChatUtils.RolePlayActionType.TODO;
import static com.github.minersstudios.msutils.utils.ChatUtils.sendRPEventMessage;

@MSCommand(command = "fart")
public class FartCommand implements MSCommandExecutor {
	private final SecureRandom random = new SecureRandom();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		}
		if (!PlayerUtils.isOnline(player)) return true;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted()) {
			return ChatUtils.sendWarning(player, Component.text("Вы замьючены"));
		}
		Location location = player.getLocation();
		boolean withPoop =
				this.random.nextInt(10) == 0
				&& location.clone().subtract(0.0d, 0.5d, 0.0d).getBlock().getType().isSolid()
				&& BlockUtils.REPLACE.contains(location.clone().getBlock().getType());
		for (Entity nearbyEntity : player.getWorld().getNearbyEntities(location.getBlock().getLocation().add(0.5d, 0.5d, 0.5d), 0.5d, 0.5d, 0.5d)) {
			if (nearbyEntity.getType() != EntityType.DROPPED_ITEM && nearbyEntity.getType() != EntityType.PLAYER) {
				withPoop = false;
				break;
			}
		}
		player.getWorld().playSound(location.add(0, 0.4, 0), Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1, 1);
		player.getWorld().spawnParticle(Particle.REDSTONE, location, 15, 0.0D, 0.0D, 0.0D, 0.5D, new Particle.DustOptions(Color.fromBGR(33, 54, 75), 10));
		if (withPoop) {
			MSDecorUtils.placeCustomDecor(
					location.getBlock(),
					player,
					"msdecor:poop",
					BlockFace.UP,
					null,
					ChatUtils.createDefaultStyledText("Какашка " + ChatUtils.serializeLegacyComponent(playerInfo.getDefaultName()))
			);
		}
		if (args.length > 0) {
			return sendRPEventMessage(player, Component.text(ChatUtils.extractMessage(args, 0)), Component.text(withPoop ? "пукнув с подливой" : "пукнув"), TODO);
		}
		return sendRPEventMessage(player, Component.text(playerInfo.getPronouns().getFartMessage()).append(Component.text(withPoop ? " с подливой" : "")), ME);
	}
}
