package github.minersStudios.msUtils.commands.roleplay;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SpitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, "Только игрок может использовать эту команду!");
			return true;
		} else if (player.getWorld() != Main.worldDark && Main.authmeApi.isAuthenticated(player)) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			if (playerInfo.isMuted()) return ChatUtils.sendWarning(player, "Вы замучены");
			player.getWorld().spawnEntity(
					player.getLocation().toVector().add(player.getLocation().getDirection().multiply(0.8d)).toLocation(player.getWorld()).add(0.0d, 1.0d, 0.0d),
					EntityType.LLAMA_SPIT
			).setVelocity(player.getEyeLocation().getDirection().multiply(1));
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 1.0f, 1.0f);
			ChatUtils.sendRPEventMessage(player, 25,
					"* "
					+ playerInfo.getGrayIDGoldName() + " "
					+ ChatColor.GOLD + playerInfo.getPronouns().getSpitMessage()
					+ "*"
			);
		}
		return true;
	}
}
