package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.DateUtils;
import com.github.minersstudios.msutils.player.PlayerFile;
import com.github.minersstudios.msutils.player.PlayerInfo;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Locale;

import static net.kyori.adventure.text.Component.text;

public class AdminBanInfoCommand {

    public static boolean runCommand(
            @NotNull CommandSender sender,
            String @NotNull [] args,
            @NotNull PlayerInfo playerInfo
    ) {
        PlayerFile playerFile = playerInfo.getPlayerFile();
        boolean banned = playerInfo.isBanned();
        boolean haveArg = args.length >= 4;
        String paramString = args.length >= 3 ? args[2].toLowerCase(Locale.ROOT) : "";
        String paramArgString = haveArg ? args[3].toLowerCase(Locale.ROOT) : "";

        if (args.length == 2) {
            ChatUtils.sendFine(sender,
                    text("Информация о бане игрока : ")
                    .append(playerInfo.getGrayIDGreenName())
                    .appendNewline()
                    .append(
                            banned
                                    ? text("    - Причина : \"")
                                    .append(text(playerFile.getBanReason()))
                                    .append(text("\""))
                                    .appendNewline()
                                    .append(text("    - Забанен до : "))
                                    .append(text(DateUtils.getSenderDate(new Date(playerFile.getBannedTo()), sender)))
                                    : text("    - Не забанен")
                    )
            );
            return true;
        }

        if (!banned) {
            ChatUtils.sendError(sender,
                    text("Данный параметр не может быть изменён/считан, так как игрок : ")
                    .append(playerInfo.getDefaultName())
                    .appendNewline()
                    .append(text("    - Не забанен"))
            );
            return true;
        }

        switch (paramString) {
            case "reason" -> {
                if (!haveArg) {
                    ChatUtils.sendFine(sender,
                            text("Причиной бана игрока : ")
                            .append(playerInfo.getGrayIDGreenName())
                            .appendNewline()
                            .append(text("    Является : \""))
                            .append(text(playerFile.getBanReason()))
                            .append(text("\""))
                    );
                    return true;
                }

                String reason = ChatUtils.extractMessage(args, 3);

                playerFile.setBanReason(reason);
                playerFile.save();
                ChatUtils.sendFine(sender,
                        text("Причина бана игрока : ")
                        .append(playerInfo.getGrayIDGreenName())
                        .appendNewline()
                        .append(text("    Была успешно изменена на : \""))
                        .append(text(reason))
                        .append(text("\""))
                );
                return true;
            }
            case "time" -> {
                if (!haveArg) {
                    ChatUtils.sendFine(sender,
                            text("Крайней датой бана игрока : ")
                            .append(playerInfo.getGrayIDGreenName())
                            .appendNewline()
                            .append(text("    Является : "))
                            .append(text(DateUtils.getSenderDate(new Date(playerFile.getBannedTo()), sender)))
                    );
                    return true;
                }

                Date date = DateUtils.getDateFromString(paramArgString, false);

                if (date == null) {
                    ChatUtils.sendError(sender, "Введите показатель в правильном формате");
                    return true;
                }

                playerFile.setBannedTo(date.getTime());
                playerFile.save();

                BanEntry banEntry = Bukkit.getBanList(BanList.Type.NAME).getBanEntry(playerInfo.getNickname());

                if (banEntry != null) {
                    banEntry.setExpiration(date);
                    banEntry.save();
                }

                ChatUtils.sendFine(sender,
                        text("Крайней датой бана игрока : ")
                        .append(playerInfo.getGrayIDGreenName())
                        .appendNewline()
                        .append(text("    Стала : "))
                        .append(text(DateUtils.getSenderDate(date, sender)))
                );
                return true;
            }
        }
        return false;
    }
}
