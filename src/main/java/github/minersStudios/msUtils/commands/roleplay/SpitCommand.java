package github.minersStudios.msUtils.commands.roleplay;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SpitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player))
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		World world = player.getWorld();
		Location location = player.getLocation();
		if (world == Main.worldDark || !Main.authmeApi.isAuthenticated(player))
			return true;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted())
			return ChatUtils.sendWarning(player, Component.text("Вы замучены"));
		world.spawnEntity(
				location.toVector().add(location.getDirection().multiply(0.8d)).toLocation(world).add(0.0d, 1.0d, 0.0d),
				EntityType.LLAMA_SPIT
		).setVelocity(player.getEyeLocation().getDirection().multiply(1));
		world.playSound(location, Sound.ENTITY_LLAMA_SPIT, 1.0f, 1.0f);
		return ChatUtils.sendRPEventMessage(player, 25, Component.text(playerInfo.getPronouns().getSpitMessage()));
	}
}