package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.listener.MSListener;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.PlayerInfoMap;
import com.github.minersstudios.msutils.player.PlayerSettings;
import com.github.minersstudios.msutils.player.ResourcePack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

@MSListener
public class PlayerResourcePackStatusListener implements Listener {

    @EventHandler
    public void onPlayerResourcePackStatus(@NotNull PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        PlayerInfoMap playerInfoMap = MSUtils.getConfigCache().playerInfoMap;
        PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(player);
        PlayerSettings playerSettings = playerInfo.getPlayerFile().getPlayerSettings();

        if (playerSettings.getResourcePackType() == ResourcePack.Type.NULL) return;
        switch (event.getStatus()) {
            case ACCEPTED -> ChatUtils.sendFine(text(player.getName()).append(text(" принял ресурспак")));
            case SUCCESSFULLY_LOADED -> {
                ChatUtils.sendFine(text(player.getName()).append(text(" успешно загрузил ресурспак")));
                if (playerInfo.isInWorldDark()) {
                    playerInfo.initJoin();
                }
            }
            case FAILED_DOWNLOAD -> {
                ChatUtils.sendWarning(text(player.getName()).append(text(" не установился ресурспак")));
                playerSettings.setResourcePackType(ResourcePack.Type.NONE);
                playerSettings.save();
                playerInfo.kickPlayer(
                        "Кажется, что-то пошло не так",
                        "Обратитесь к администрации\nА пока ваш тип ресурспака изменён на :\n \"Без текстурпака\""
                );
            }
            case DECLINED -> {
                ChatUtils.sendWarning(text(player.getName()).append(text(" не принял ресурспак")));
                playerInfo.kickPlayer(
                        "Кажется, что-то пошло не так",
                        "В настройках сервера поменяйте параметр :\n\"Наборы ресурсов\" на \"Включены\""
                );
            }
        }
    }
}
