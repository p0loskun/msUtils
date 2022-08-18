package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.classes.PlayerInfo;
import com.github.minersstudios.msutils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import javax.annotation.Nonnull;

public class AsyncPlayerPreLoginListener implements Listener {

	@EventHandler
	public void onAsyncPlayerPreLogin(@Nonnull AsyncPlayerPreLoginEvent event) {
		String hostName = event.getAddress().getHostName(),
				nickname = event.getName();
		PlayerInfo playerInfo = new PlayerInfo(event.getUniqueId(), nickname);

		if (
				playerInfo.hasPlayerDataFile()
				&& playerInfo.getIP() != null
				&& !playerInfo.getIP().contains(hostName)
		) {
			ChatUtils.sendWarning(null,
					Component.text("Игроку : \"")
							.append(playerInfo.getGrayIDGoldName())
							.append(Component.text("\" был добавлен новый айпи адресс : "))
							.append(Component.text(hostName))
			);
			playerInfo.setIP(hostName);
		}

		if (
				(playerInfo.isBanned() && playerInfo.getBannedTo() - System.currentTimeMillis() < 0)
				|| (playerInfo.isBanned()
				&& !Bukkit.getBanList(BanList.Type.NAME).isBanned(nickname))
		) {
			playerInfo.setBanned(false, null);
		}
	}
}
