package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.msutils.utils.ChatUtils;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener implements Listener {

	@EventHandler
	public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
		Player killedPlayer = event.getEntity();
		event.deathMessage(null);
		PlayerUtils.setSitting(killedPlayer, null, (String) null);
		ChatUtils.sendDeathMessage(killedPlayer, killedPlayer.getKiller());
	}
}
