package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class AdminUpdateCommand {

    public static boolean runCommand(
            @NotNull CommandSender sender,
            @NotNull PlayerInfo playerInfo
    ) {
        playerInfo.update();
        ChatUtils.sendFine(sender,
                text("Данные игрока : ")
                .append(playerInfo.getGrayIDGreenName())
                .append(text(" были успешно обновлены"))
        );
        return true;
    }
}
