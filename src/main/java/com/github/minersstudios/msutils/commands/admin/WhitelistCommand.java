package com.github.minersstudios.msutils.commands.admin;

import com.github.minersstudios.mscore.command.MSCommand;
import com.github.minersstudios.mscore.command.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.IDUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.kyori.adventure.text.Component.text;

@MSCommand(
        command = "whitelist",
        usage = " ꀑ §cИспользуй: /<command> [add/remove/reload] [id/никнейм]",
        description = "Удаляет/добавляет игрока в вайтлист, или перезагружает его",
        permission = "msutils.whitelist",
        permissionDefault = PermissionDefault.OP
)
public class WhitelistCommand implements MSCommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            String @NotNull ... args
    ) {
        if (args.length == 0) return false;

        if (args[0].equalsIgnoreCase("reload")) {
            Bukkit.reloadWhitelist();
            ChatUtils.sendFine(sender, "Вайт-Лист был перезагружен");
            return true;
        }

        if (args.length > 1 && IDUtils.matchesIDRegex(args[1])) {
            if (args[0].equalsIgnoreCase("add")) {
                ChatUtils.sendWarning(sender, "Для добавления игрока используйте ник!");
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                OfflinePlayer offlinePlayer = getConfigCache().idMap.getPlayerByID(args[1]);

                if (offlinePlayer == null || offlinePlayer.getName() == null) {
                    ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
                    return true;
                }

                PlayerInfo playerInfo = MSUtils.getConfigCache().playerInfoMap.getPlayerInfo(offlinePlayer.getUniqueId(), offlinePlayer.getName());

                if (playerInfo.setWhiteListed(false)) {
                    ChatUtils.sendFine(sender,
                            text("Игрок : \"")
                            .append(playerInfo.getGrayIDGreenName())
                            .append(text(" ("))
                            .append(text(offlinePlayer.getName()))
                            .append(text(")\" был удалён из белого списка"))
                    );
                    return true;
                }

                ChatUtils.sendWarning(sender,
                        text("Игрок : \"")
                        .append(playerInfo.getGrayIDGoldName())
                        .append(text(" ("))
                        .append(text(offlinePlayer.getName()))
                        .append(text(")\" не состоит в белом списке"))
                );
                return true;
            }
            return false;
        }
        if (args.length > 1 && args[1].length() > 2) {
            OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[1]);

            if (offlinePlayer == null) {
                ChatUtils.sendError(sender, "Данного игрока не существует");
                return true;
            }

            PlayerInfo playerInfo = MSUtils.getConfigCache().playerInfoMap.getPlayerInfo(offlinePlayer.getUniqueId(), args[1]);

            if (args[0].equalsIgnoreCase("add")) {
                if (playerInfo.setWhiteListed(true)) {
                    ChatUtils.sendFine(sender,
                            text("Игрок : \"")
                            .append(playerInfo.getGrayIDGreenName())
                            .append(text(" ("))
                            .append(text(args[1]))
                            .append(text(")\" был добавлен в белый список"))
                    );
                    return true;
                }

                ChatUtils.sendWarning(sender,
                        text("Игрок : \"")
                        .append(playerInfo.getGrayIDGoldName())
                        .append(text(" ("))
                        .append(text(args[1]))
                        .append(text(")\" уже состоит в белом списке"))
                );
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (playerInfo.setWhiteListed(false)) {
                    ChatUtils.sendFine(sender,
                            text("Игрок : \"")
                            .append(playerInfo.getGrayIDGreenName())
                            .append(text(" ("))
                            .append(text(args[1]))
                            .append(text(")\" был удалён из белого списка"))
                    );
                    return true;
                }

                ChatUtils.sendWarning(sender,
                        text("Игрок : \"")
                        .append(playerInfo.getGrayIDGoldName())
                        .append(text(" ("))
                        .append(text(args[1]))
                        .append(text(")\" не состоит в белом списке"))
                );
                return true;
            }
            return false;
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
        if (args.length == 1) {
            completions.add("add");
            completions.add("remove");
            completions.add("reload");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            for (OfflinePlayer offlinePlayer : Bukkit.getWhitelistedPlayers()) {
                PlayerInfo playerInfo = MSUtils.getConfigCache().playerInfoMap.getPlayerInfo(offlinePlayer.getUniqueId(), args[1]);
                int id = playerInfo.getID(false, false);

                if (id != -1) {
                    completions.add(String.valueOf(id));
                }

                completions.add(offlinePlayer.getName());
            }
        }
        return completions;
    }

    @Override
    public @Nullable CommandNode<?> getCommandNode() {
        return literal("whitelist")
                .then(literal("add").then(argument("никнейм", StringArgumentType.word())))
                .then(literal("remove").then(argument("id/никнейм", StringArgumentType.word())))
                .then(literal("reload"))
                .build();
    }
}
