package com.github.minersstudios.msutils.commands.roleplay;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.github.minersstudios.msutils.utils.ChatUtils.RolePlayActionType.ME;
import static com.github.minersstudios.msutils.utils.ChatUtils.RolePlayActionType.TODO;
import static com.github.minersstudios.msutils.utils.ChatUtils.sendRPEventMessage;

@MSCommand(
		command = "spit",
		usage = " ꀑ §cИспользуй: /<command> [речь]",
		description = "Покажи свою дерзость и плюнь кому-то в лицо"
)
public class SpitCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
			return true;
		}
		World world = player.getWorld();
		Location location = player.getLocation();
		if (!PlayerUtils.isOnline(player)) return true;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted()) {
			ChatUtils.sendWarning(player, Component.text("Вы замьючены"));
			return true;
		}
		world.spawnEntity(
				location.toVector().add(location.getDirection().multiply(0.8d)).toLocation(world).add(0.0d, 1.0d, 0.0d),
				EntityType.LLAMA_SPIT
		).setVelocity(player.getEyeLocation().getDirection().multiply(1));
		world.playSound(location, Sound.ENTITY_LLAMA_SPIT, SoundCategory.PLAYERS, 1.0f, 1.0f);
		if (args.length > 0) {
			sendRPEventMessage(player, Component.text(ChatUtils.extractMessage(args, 0)), Component.text("плюнув"), TODO);
			return true;
		}
		sendRPEventMessage(player, Component.text(playerInfo.getPronouns().getSpitMessage()), ME);
		return true;
	}
}
