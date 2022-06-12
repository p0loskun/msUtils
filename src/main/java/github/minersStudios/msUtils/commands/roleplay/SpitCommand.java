package github.minersStudios.msUtils.commands.roleplay;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
        } else {
            if (player.getWorld() == Main.worldDark || !Main.authmeApi.isAuthenticated(player)) return true;
            PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
            if (!playerInfo.isMuted()) {
                Location location = player.getLocation().toVector().add(player.getLocation().getDirection().multiply(0.8D)).toLocation(player.getWorld()).add(0.0D, 1.0D, 0.0D);
                player.getWorld().spawnEntity(location, EntityType.LLAMA_SPIT).setVelocity(player.getEyeLocation().getDirection().multiply(1));
                String message = playerInfo.getPronouns() != null ? playerInfo.getPronouns().getSpitMessage() : Pronouns.HE.getSpitMessage();
                ChatUtils.sendMessage(player, 25, " ꀓ " + ChatColor.GOLD + "*" + ChatColor.GRAY + " [" + playerInfo.getID() + "] " + ChatColor.GOLD + playerInfo.getFirstname() + " " + playerInfo.getLastname() + " " + message + "*");
                DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), "*" + " [" + playerInfo.getID() + "] " + playerInfo.getFirstname() + " " + playerInfo.getLastname() + " " + message + "*");
            } else {
                ChatUtils.sendWarning(player, "Вы замучены");
            }
        }
        return true;
    }

}