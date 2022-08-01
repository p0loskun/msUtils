package com.github.MinersStudios.msUtils.listeners.player;

import com.github.MinersStudios.msUtils.classes.PlayerInfo;
import com.github.MinersStudios.msUtils.utils.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;

public class PlayerInteractEntityListener implements Listener {

	@EventHandler
	public void onPlayerInteractEntity(@Nonnull PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Player clickedPlayer) {
			PlayerInfo playerInfo = new PlayerInfo(clickedPlayer.getUniqueId());
			event.getPlayer().sendActionBar(
					Component.text()
							.append(playerInfo.getGoldenName())
							.append(Component.text(" "))
							.append(Component.text(playerInfo.getPatronymic()))
							.color(Config.Colors.joinMessageColorPrimary)
							.build()
			);
		}
	}
}
