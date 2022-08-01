package github.minersStudios.msUtils.commands.teleport;

import github.minersStudios.msUtils.classes.PlayerID;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.annotation.Nonnull;

public class TeleportToLastDeathLocationCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length < 1) return false;
        if (args[0].matches("[0-99]+")) {
            OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
            if (offlinePlayer == null) {
                return ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
            }
            return teleportToLastDeathLocation(sender, offlinePlayer);
        }
        if (args[0].length() > 2) {
            OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
            if (offlinePlayer == null) {
                return ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
            }
            return teleportToLastDeathLocation(sender, offlinePlayer);
        }
        return ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
    }

    private static boolean teleportToLastDeathLocation(@Nonnull CommandSender sender, @Nonnull OfflinePlayer offlinePlayer) {
        if (!offlinePlayer.hasPlayedBefore() || offlinePlayer.getName() == null) {
            return ChatUtils.sendWarning(sender, Component.text("Данный игрок ещё ни разу не играл на сервере"));
        }
        PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
        Location lastDeathLocation = offlinePlayer.getLastDeathLocation();
        if (offlinePlayer.getPlayer() == null) {
            return ChatUtils.sendWarning(sender,
                    Component.text("Игрок : \"")
                            .append(playerInfo.getGrayIDGreenName())
                            .append(Component.text(" ("))
                            .append(Component.text(offlinePlayer.getName()))
                            .append(Component.text(")\" не в сети!"))
            );
        }
        if (lastDeathLocation == null) {
            return ChatUtils.sendWarning(sender,
                    Component.text("Игрок : \"")
                            .append(playerInfo.getGrayIDGreenName())
                            .append(Component.text(" ("))
                            .append(Component.text(offlinePlayer.getName()))
                            .append(Component.text(")\" не имеет последней точки смерти!"))
            );
        }
        offlinePlayer.getPlayer().teleportAsync(lastDeathLocation.add(0.5d, 0.0d, 0.5d), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return ChatUtils.sendFine(sender,
                Component.text("Игрок : \"")
                .append(playerInfo.getGrayIDGreenName())
                .append(Component.text(" ("))
                .append(Component.text(offlinePlayer.getName()))
                .append(Component.text(")\" был телепортирован на последние координаты смерти"))
        );
    }
}
