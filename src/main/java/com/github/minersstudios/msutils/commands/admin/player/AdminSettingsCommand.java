package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.PlayerSettings;
import com.github.minersstudios.msutils.player.ResourcePack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static net.kyori.adventure.text.Component.text;

public class AdminSettingsCommand {

    public static boolean runCommand(
            @NotNull CommandSender sender,
            String @NotNull [] args,
            @NotNull PlayerInfo playerInfo
    ) {
        if (args.length < 3) {
            ChatUtils.sendError(sender, "Используйте один из доступных вариантов :\n    resourcepack-type");
            return true;
        }

        PlayerSettings playerSettings = playerInfo.getPlayerFile().getPlayerSettings();
        boolean haveArg = args.length >= 4;
        String paramString = args[2].toLowerCase(Locale.ENGLISH);
        String paramArgString = haveArg ? args[3].toLowerCase(Locale.ENGLISH) : "";

        switch (paramString) {
            case "resourcepack-type" -> {
                if (!haveArg) {
                    ResourcePack.Type type = playerSettings.getResourcePackType();
                    ChatUtils.sendFine(sender,
                            text("Тип ресурс-пака игрока : ")
                            .append(playerInfo.getGrayIDGreenName())
                            .appendNewline()
                            .append(text("    Равен : \""))
                            .append(text(type.name().toLowerCase(Locale.ENGLISH)))
                            .append(text("\""))
                    );
                    return true;
                }

                ResourcePack.Type type = switch (paramArgString) {
                    case "full" -> ResourcePack.Type.FULL;
                    case "lite" -> ResourcePack.Type.LITE;
                    case "none" -> ResourcePack.Type.NONE;
                    case "null" -> ResourcePack.Type.NULL;
                    default -> null;
                };

                if (type == null) {
                    ChatUtils.sendError(sender, "Используйте один из доступных вариантов :\n    full, lite, none, null");
                    return true;
                }

                playerSettings.setResourcePackType(type);
                playerSettings.save();

                if (type == ResourcePack.Type.NONE || type == ResourcePack.Type.NULL) {
                    playerInfo.kickPlayer("Вы были кикнуты", "Этот параметр требует повторного захода на сервер");
                }

                ChatUtils.sendFine(sender,
                        text("Тип ресурс-пака игрока : ")
                        .append(playerInfo.getGrayIDGreenName())
                        .appendNewline()
                        .append(text("    Был успешно изменён на : \""))
                        .append(text(paramArgString))
                        .append(text("\""))
                );
                return true;
            }
        }
        return false;
    }
}
