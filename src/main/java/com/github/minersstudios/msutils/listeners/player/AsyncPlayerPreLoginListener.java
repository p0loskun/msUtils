package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.listener.MSListener;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.DateUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerFile;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Date;

import static net.kyori.adventure.text.Component.text;

@MSListener
public class AsyncPlayerPreLoginListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(@NotNull AsyncPlayerPreLoginEvent event) {
        String hostAddress = event.getAddress().getHostAddress();
        String nickname = event.getName();
        PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(event.getUniqueId(), nickname);
        PlayerFile playerFile = playerInfo.getPlayerFile();
        OfflinePlayer offlinePlayer = playerInfo.getOfflinePlayer();

        if (
                playerFile.exists()
                && !playerFile.getIpList().contains(hostAddress)
        ) {
            playerFile.addIp(hostAddress);
            playerFile.save();

            ChatUtils.sendWarning(
                    text("Игроку : \"")
                    .append(playerInfo.getGrayIDGoldName())
                    .append(text("\" был добавлен новый айпи адрес : "))
                    .append(text(hostAddress))
            );
        }

        if (
                (playerInfo.getPlayerFile().isBanned() && playerInfo.getBannedTo() - System.currentTimeMillis() < 0)
                || (playerInfo.getPlayerFile().isBanned() && !Bukkit.getBanList(BanList.Type.NAME).isBanned(nickname))
        ) {
            playerInfo.setBanned(false);
        }

        if (MSUtils.getConfigCache().developerMode && !offlinePlayer.isOp()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    Component.empty()
                    .append(text("Вы были кикнуты", Style.style(NamedTextColor.RED, TextDecoration.BOLD)))
                    .append(text("\n\n<---====+====--->\n", NamedTextColor.DARK_GRAY))
                    .append(text("\n\nПроводятся тех-работы\nВ скором времени вы сможете зайти\n\nhttps:\\\\whomine.net\\discord\n", NamedTextColor.GRAY))
                    .append(text("\n\n<---====+====--->\n", NamedTextColor.DARK_GRAY))
            );
        }

        if (!Bukkit.getWhitelistedPlayers().contains(offlinePlayer)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST,
                    Component.empty()
                    .append(text("Вас нету в вайт-листе!", Style.style(NamedTextColor.RED, TextDecoration.BOLD)))
                    .append(text("\n\n<---====+====--->\n", NamedTextColor.DARK_GRAY))
                    .append(text("\n\nПохоже, вы ещё нам неизвестны\nПожалуйста, обратитесь к администрации\n\nhttps:\\\\whomine.net\\discord\n", NamedTextColor.GRAY))
                    .append(text("\n\n<---====+====--->\n", NamedTextColor.DARK_GRAY))
            );
        }

        if (Bukkit.getBannedPlayers().contains(offlinePlayer)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                    Component.empty()
                    .append(text("Вы всё ещё забанены!", Style.style(NamedTextColor.RED, TextDecoration.BOLD)))
                    .append(text("\n\n<---====+====--->\n", NamedTextColor.DARK_GRAY))
                    .append(text("\nПричина :\n\""))
                    .append(text(playerInfo.getBanReason()))
                    .append(text("\"\nДо :\n"))
                    .append(text(DateUtils.getDate(Date.from(Instant.ofEpochMilli(playerInfo.getBannedTo())), event.getAddress())))
                    .color(NamedTextColor.GRAY)
                    .append(text("\n\n<---====+====--->\n", NamedTextColor.DARK_GRAY))
            );
        }
    }
}
