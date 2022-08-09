package com.github.minersstudios.msUtils.listeners.player;

import com.github.minersstudios.msUtils.utils.ChatUtils;
import com.github.minersstudios.msUtils.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

public class PlayerDeathListener implements Listener {

	@EventHandler
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		Player killedPlayer = event.getEntity();
		event.deathMessage(null);
		PlayerUtils.setSitting(killedPlayer, null, null);
		ChatUtils.sendDeathMessage(killedPlayer, killedPlayer.getKiller());
	}
}
