package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.DateUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Locale;

import static net.kyori.adventure.text.Component.text;

public class AdminMuteInfoCommand {

    public static boolean runCommand(
            @NotNull CommandSender sender,
            String @NotNull [] args,
            @NotNull PlayerInfo playerInfo
    ) {
        boolean muted = playerInfo.isMuted();
        boolean haveArg = args.length >= 4;
        String paramString = args.length >= 3 ? args[2].toLowerCase(Locale.ROOT) : "";
        String paramArgString = haveArg ? args[3].toLowerCase(Locale.ROOT) : "";

        if (args.length == 2) {
            ChatUtils.sendFine(sender,
                    text("Информация о мьюте игрока : ")
                    .append(playerInfo.getGrayIDGreenName())
                    .appendNewline()
                    .append(
                            muted
                                    ? text("    - Причина : \"")
                                    .append(text(playerInfo.getMuteReason()))
                                    .append(text("\""))
                                    .appendNewline()
                                    .append(text("    - Замьючен до : "))
                                    .append(text(DateUtils.getSenderDate(new Date(playerInfo.getMutedTo()), sender)))
                                    : text("    - Не замьючен")
                    )
            );
            return true;
        }

        if (!muted) {
            ChatUtils.sendError(sender,
                    text("Данный параметр не может быть изменён/считан, так как игрок : ")
                    .append(playerInfo.getDefaultName())
                    .appendNewline()
                    .append(text("    - Не замьючен"))
            );
            return true;
        }

        switch (paramString) {
            case "reason" -> {
                if (!haveArg) {
                    ChatUtils.sendFine(sender,
                            text("Причиной мьюта игрока : ")
                            .append(playerInfo.getGrayIDGreenName())
                            .appendNewline()
                            .append(text("    Является : \""))
                            .append(text(playerInfo.getMuteReason()))
                            .append(text("\""))
                    );
                    return true;
                }

                String reason = ChatUtils.extractMessage(args, 3);

                MSUtils.getConfigCache().muteMap.put(playerInfo.getOfflinePlayer(), playerInfo.getMutedTo(), reason);
                ChatUtils.sendFine(sender,
                        text("Причина мьюта игрока : ")
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
                            text("Крайней датой мьюта игрока : ")
                            .append(playerInfo.getGrayIDGreenName())
                            .appendNewline()
                            .append(text("    Является : "))
                            .append(text(DateUtils.getSenderDate(new Date(playerInfo.getMutedTo()), sender)))
                    );
                    return true;
                }

                Date date = DateUtils.getDateFromString(paramArgString, false);

                if (date == null) {
                    ChatUtils.sendError(sender, "Введите показатель в правильном формате");
                    return true;
                }

                MSUtils.getConfigCache().muteMap.put(playerInfo.getOfflinePlayer(), date.getTime(), playerInfo.getMuteReason());
                ChatUtils.sendFine(sender,
                        text("Крайней датой мьюта игрока : ")
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
