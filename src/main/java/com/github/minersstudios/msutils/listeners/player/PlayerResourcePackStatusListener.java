package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.PlayerSettings;
import com.github.minersstudios.msutils.player.ResourcePack;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerResourcePackStatusListener implements Listener {

	@EventHandler
	public void onPlayerResourcePackStatus(@NotNull PlayerResourcePackStatusEvent event) {
		Player player = event.getPlayer();
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		PlayerSettings playerSettings = playerInfo.getPlayerFile().getPlayerSettings();

		if (playerSettings.getResourcePackType() == null) return;
		switch (event.getStatus()) {
			case ACCEPTED -> ChatUtils.sendFine(null, Component.text(player.getName()).append(Component.text(" принял ресурспак")));
			case SUCCESSFULLY_LOADED -> {
				ChatUtils.sendFine(null, Component.text(player.getName()).append(Component.text(" успешно загрузил ресурспак")));
				if (player.getWorld() == MSUtils.getWorldDark()) {
					playerInfo.teleportToLastLeaveLocation();
				}
			}
			case FAILED_DOWNLOAD -> {
				ChatUtils.sendWarning(null, Component.text(player.getName()).append(Component.text(" не установился ресурспак")));
				playerSettings.setResourcePackType(ResourcePack.Type.NONE);
				playerSettings.save();
				PlayerUtils.kickPlayer(player, "Кажется, что-то пошло не так", "Обратитесь к администрации\nА пока ваш тип ресурспака изменён на :\n \"Без текстурпака\"");
			}
			case DECLINED -> {
				ChatUtils.sendWarning(null, Component.text(player.getName()).append(Component.text(" не принял ресурспак")));
				PlayerUtils.kickPlayer(player, "Кажется, что-то пошло не так", "В настройках сервера поменяйте параметр :\n\"Наборы ресурсов\" на \"Включены\"");
			}
		}
	}
}
