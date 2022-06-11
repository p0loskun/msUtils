package github.minersStudios.msUtils.commands.roleplay;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.CommandUtils;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class MeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtils.sendError(sender, "Только игрок может использовать эту команду!");
            return true;
        } else {
            if (args.length < 1) {
                return false;
            } else {
                if (player.getWorld() == Main.worldDark || !Main.authmeApi.isAuthenticated(player)) return true;
                PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
                if (!playerInfo.isMuted()) {
                    String message = CommandUtils.extractMessage(args, 0);
                    CommandUtils.sendMessage(player, 25, " ꀓ " + ChatColor.GOLD + "*" + ChatColor.GRAY + " [" + playerInfo.getID() + "] " + ChatColor.GOLD + playerInfo.getFirstname() + " " + playerInfo.getLastname() + " " + message + "*");
                    DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), "*" + " [" + playerInfo.getID() + "] " + playerInfo.getFirstname() + " " + playerInfo.getLastname() + " " + message + "*");
                } else {
                    ChatUtils.sendWarning(player, "Вы замучены");
                }
            }
        }
        return true;
    }

}