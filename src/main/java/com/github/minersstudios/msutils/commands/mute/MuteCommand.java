package com.github.minersstudios.msutils.commands.mute;

import com.github.minersstudios.mscore.command.MSCommand;
import com.github.minersstudios.mscore.command.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.DateUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.utils.IDUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

@MSCommand(
        command = "mute",
        usage = " ꀑ §cИспользуй: /<command> [id/никнейм] [время][s/m/h/d/M/y] [причина]",
        description = "Покажи кто тут главный и замьють игрока",
        permission = "msutils.mute",
        permissionDefault = PermissionDefault.OP
)
public class MuteCommand implements MSCommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            String @NotNull ... args
    ) {
        if (args.length < 2) return false;

        Date date = DateUtils.getDateFromString(args[1], false);

        if (date == null) {
            ChatUtils.sendError(sender, "Введите показатель в правильном формате");
            return true;
        }

        String reason = args.length > 2
                ? ChatUtils.extractMessage(args, 2)
                : "неизвестно";

        if (IDUtils.matchesIDRegex(args[0])) {
            OfflinePlayer offlinePlayer = getConfigCache().idMap.getPlayerByID(args[0]);

            if (
                    offlinePlayer == null
                    || StringUtils.isBlank(offlinePlayer.getName())
            ) {
                ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
                return true;
            }

            MSUtils.getConfigCache().playerInfoMap.getPlayerInfo(offlinePlayer.getUniqueId(), offlinePlayer.getName())
                    .setMuted(true, date, reason, sender);
            return true;
        }
        if (args[0].length() > 2) {
            String name = args[0];
            OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(name);

            if (
                    offlinePlayer == null
                    || !offlinePlayer.hasPlayedBefore()
            ) {
                ChatUtils.sendError(sender, "Этот игрок ещё ни разу не играл на сервере");
                return true;
            }

            MSUtils.getConfigCache().playerInfoMap.getPlayerInfo(offlinePlayer.getUniqueId(), name)
                    .setMuted(true, date, reason, sender);
            return true;
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
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    String nickname = offlinePlayer.getName();
                    UUID uuid = offlinePlayer.getUniqueId();

                    System.out.println(getConfigCache().muteMap.getMap().keySet());
                    if (StringUtils.isBlank(nickname) || getConfigCache().muteMap.isMuted(offlinePlayer)) continue;

                    int id = getConfigCache().idMap.getID(uuid, false, false);

                    if (id != -1) {
                        completions.add(String.valueOf(id));
                    }

                    if (offlinePlayer.hasPlayedBefore()) {
                        completions.add(nickname);
                    }
                }
            }
            case 2 -> {
                return DateUtils.getTimeSuggestions(args[1]);
            }
        }
        return completions;
    }

    @Override
    public @Nullable CommandNode<?> getCommandNode() {
        return literal("mute")
                .then(
                        argument("id/никнейм", StringArgumentType.word())
                        .then(
                                argument("время", StringArgumentType.word())
                                .then(argument("причина", StringArgumentType.greedyString()))
                        )
                ).build();
    }
}
