package com.github.minersstudios.msutils.commands.admin.msutils;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.config.ConfigCache;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class UpdateMutesCommand {

    public static void runCommand(@NotNull CommandSender sender) {
        long time = System.currentTimeMillis();
        ConfigCache configCache = MSUtils.getConfigCache();

        configCache.muteMap.reloadMutes();

        ChatUtils.sendFine(sender,
                text("Список мутов был успешно перезагружен за ")
                .append(text(System.currentTimeMillis() - time))
                .append(text("ms"))
        );
    }
}
