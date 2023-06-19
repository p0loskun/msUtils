package com.github.minersstudios.msutils.commands.teleport;

import com.github.minersstudios.mscore.command.MSCommand;
import com.github.minersstudios.mscore.command.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.IDUtils;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

@MSCommand(
        command = "worldteleport",
        aliases = {
                "worldtp",
                "wtp",
                "teleportworld",
                "tpworld",
                "tpw"
        },
        usage = " ꀑ §cИспользуй: /<command> [id/никнейм] [world name] [x] [y] [z]",
        description = "Телепортирует игрока на координаты в указанном мире, если координаты не указаны, телепортирует на точку спавна данного мира",
        permission = "msutils.worldteleport",
        permissionDefault = PermissionDefault.OP
)
public class WorldTeleportCommand implements MSCommandExecutor {
    private static final String coordinatesRegex = "^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$";

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            String @NotNull ... args
    ) {
        if (args.length < 2) return false;

        if (IDUtils.matchesIDRegex(args[0])) {
            OfflinePlayer offlinePlayer = IDUtils.getPlayerByID(args[0]);

            if (offlinePlayer == null) {
                ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
                return true;
            }
            return teleportToWorld(sender, offlinePlayer, args);
        }

        if (args[0].length() > 2) {
            OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);

            if (offlinePlayer == null) {
                ChatUtils.sendError(sender, "Данного игрока не существует");
                return true;
            }
            return teleportToWorld(sender, offlinePlayer, args);
        }

        ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            String @NotNull ... args
    ) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(player);
                    int id = playerInfo.getID(false, false);

                    if (id != -1) {
                        completions.add(String.valueOf(id));
                    }

                    completions.add(player.getName());
                }
            }
            case 2 -> {
                for (World world : Bukkit.getWorlds()) {
                    if (world != MSUtils.getWorldDark()) {
                        completions.add(world.getName());
                    }
                }
            }
            case 3, 4, 5 -> {
                Location playerLoc = null;

                if (sender instanceof Player player && args[1].equals(player.getWorld().getName())) {
                    playerLoc = player.getLocation();
                }

                if (playerLoc != null) {
                    double coordinate = switch (args.length) {
                        case 3 -> playerLoc.x();
                        case 4 -> playerLoc.y();
                        default -> playerLoc.z();
                    };
                    double rounded = Math.round(coordinate * 100) / 100.0;

                    completions.add(String.valueOf(rounded));
                }
            }
        }
        return completions;
    }

    private static boolean teleportToWorld(
            @NotNull CommandSender sender,
            @NotNull OfflinePlayer offlinePlayer,
            String @NotNull ... args
    ) {
        if (!offlinePlayer.hasPlayedBefore() || offlinePlayer.getName() == null) {
            ChatUtils.sendWarning(sender, "Данный игрок ещё ни разу не играл на сервере");
            return true;
        }
        PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), offlinePlayer.getName());
        if (offlinePlayer.getPlayer() == null) {
            ChatUtils.sendWarning(sender,
                    text("Игрок : \"")
                    .append(playerInfo.getGrayIDGoldName())
                    .append(text("\" не в сети!"))
            );
            return true;
        }
        World world = Bukkit.getWorld(args[1]);
        if (world == null) {
            ChatUtils.sendWarning(sender, "Такого мира не существует!");
            return true;
        }
        Location spawnLoc = world.getSpawnLocation();
        double
                x = spawnLoc.getX(),
                y = spawnLoc.getY(),
                z = spawnLoc.getZ();
        if (args.length > 2) {
            if (
                    args.length != 5
                    || !args[2].matches(coordinatesRegex)
                    || !args[3].matches(coordinatesRegex)
                    || !args[4].matches(coordinatesRegex)
            ) return false;
            x = Double.parseDouble(args[2]);
            y = Double.parseDouble(args[3]);
            z = Double.parseDouble(args[4]);
            if (x > 29999984 || z > 29999984) {
                ChatUtils.sendWarning(sender, "Указаны слишком большие координаты!");
                return true;
            }
        }
        offlinePlayer.getPlayer().teleportAsync(new Location(world, x, y, z), PlayerTeleportEvent.TeleportCause.PLUGIN);
        ChatUtils.sendFine(sender,
                text("Игрок : \"")
                .append(playerInfo.getGrayIDGreenName())
                .append(text(" ("))
                .append(text(playerInfo.getNickname()))
                .append(text(")\" был телепортирован :"))
                .append(text("\n    - Мир : "))
                .append(text(world.getName()))
                .append(text("\n    - X : "))
                .append(text(x))
                .append(text("\n    - Y : "))
                .append(text(y))
                .append(text("\n    - Z : "))
                .append(text(z))
        );
        return true;
    }

    @Override
    public @Nullable CommandNode<?> getCommandNode() {
        return LiteralArgumentBuilder.literal("worldteleport")
                .then(
                        RequiredArgumentBuilder.argument("id/никнейм", StringArgumentType.word())
                        .then(
                                RequiredArgumentBuilder.argument("мир", DimensionArgument.dimension())
                                .then(RequiredArgumentBuilder.argument("координаты", Vec3Argument.vec3()))
                        )
                ).build();
    }
}
