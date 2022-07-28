package github.minersStudios.msUtils.commands.roleplay;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class MeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player))
			return ChatUtils.sendError(sender, "Только игрок может использовать эту команду!");
		if (player.getWorld() == Main.worldDark || !Main.authmeApi.isAuthenticated(player))
			return true;
		if (args.length < 1)
			return false;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted())
			return ChatUtils.sendWarning(player, "Вы замучены");
		return ChatUtils.sendRPEventMessage(player, 25,
				"* "
				+ playerInfo.getGrayIDGoldName() + " "
				+ ChatColor.GOLD + ChatUtils.extractMessage(args, 0)
				+ "*"
		);
	}
}