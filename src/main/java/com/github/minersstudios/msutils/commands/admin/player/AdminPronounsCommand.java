package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerFile;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.Pronouns;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static net.kyori.adventure.text.Component.text;

public class AdminPronounsCommand {

    public static boolean runCommand(
            @NotNull CommandSender sender,
            String @NotNull [] args,
            @NotNull PlayerInfo playerInfo
    ) {
        PlayerFile playerFile = playerInfo.getPlayerFile();

        if (args.length == 2) {
            ChatUtils.sendFine(sender,
                    text("Местоимение игрока : ")
                    .append(playerInfo.getGrayIDGreenName())
                    .appendNewline()
                    .append(text("    Равно : \""))
                    .append(text(playerFile.getPronouns().name().toLowerCase(Locale.ENGLISH)))
                    .append(text("\""))
            );
            return true;
        } else if (args.length == 3) {
            String pronounsString = args[2];

            Pronouns pronouns;
            try {
                pronouns = Pronouns.valueOf(pronounsString.toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException ignore) {
                ChatUtils.sendError(sender, "Используйте один из доступных вариантов :\n    he, she, they");
                return true;
            }

            playerFile.setPronouns(pronouns);
            playerFile.save();
            ChatUtils.sendFine(sender,
                    text("Местоимение игрока : ")
                    .append(playerInfo.getGrayIDGreenName())
                    .appendNewline()
                    .append(text("    Было успешно изменено на : \""))
                    .append(text(pronounsString.toLowerCase(Locale.ENGLISH)))
                    .append(text("\""))
            );
            return true;
        }
        return false;
    }
}
