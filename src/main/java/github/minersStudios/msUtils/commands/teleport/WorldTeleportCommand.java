package github.minersStudios.msUtils.commands.teleport;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerID;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.annotation.Nonnull;

public class WorldTeleportCommand implements CommandExecutor {
    private static final String coordinatesRegex = "^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$";
    private static double x, y, z;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length < 2)
            return false;
        if (args[0].matches("[0-99]+")) {
            OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
            if (offlinePlayer == null)
                return ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
            return teleportToWorld(args, sender, offlinePlayer);
        }
        if (args[0].length() > 2) {
            OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
            if (offlinePlayer == null)
                return ChatUtils.sendError(sender, "Что-то пошло не так...");
            return teleportToWorld(args, sender, offlinePlayer);
        }
        return ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
    }

    private static boolean teleportToWorld(@Nonnull String[] args, @Nonnull CommandSender sender, @Nonnull OfflinePlayer offlinePlayer) {
        if (!offlinePlayer.hasPlayedBefore())
            return ChatUtils.sendWarning(sender, "Данный игрок ещё ни разу не играл на сервере");
        PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
        if (offlinePlayer.getPlayer() == null)
            return ChatUtils.sendWarning(sender, "Игрок : \"" + playerInfo.getGrayIDGoldName() + "\" не в сети!");
        World world = Bukkit.getWorld(args[1]);
        if (world == null)
            return ChatUtils.sendWarning(sender, "Такого мира не существует!");
        Location spawnLoc = world.getSpawnLocation();
        x = spawnLoc.getX();
        y = spawnLoc.getY();
        z = spawnLoc.getZ();
        if (args.length > 2) {
            if (args.length != 5 || !args[2].matches(coordinatesRegex) || !args[3].matches(coordinatesRegex) || !args[4].matches(coordinatesRegex))
                return false;
            x = Double.parseDouble(args[2]);
            y = Double.parseDouble(args[3]);
            z = Double.parseDouble(args[4]);
            if (x > 29999984 || z > 29999984)
                return ChatUtils.sendWarning(sender, "Указаны слишком большие координаты!");
        }
        Bukkit.getScheduler().runTask(Main.plugin, () -> offlinePlayer.getPlayer().teleport(new Location(world, x, y, z), PlayerTeleportEvent.TeleportCause.PLUGIN));
        return ChatUtils.sendFine(sender, "Игрок : \"" + playerInfo.getDefaultName() + " (" + offlinePlayer.getName() + ")\" был телепортирован :"
                + "\n    - Мир : " + world.getName()
                + "\n    - X : " + x
                + "\n    - Y : " + y
                + "\n    - Z : " + z
        );
    }
}
