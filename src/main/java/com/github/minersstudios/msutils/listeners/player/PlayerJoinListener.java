package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.*;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerJoinListener implements Listener {

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(player);
		PlayerFile playerFile = playerInfo.getPlayerFile();
		MSUtils.getScoreboardHideTagsTeam().addEntry(player.getName());
		player.setScoreboard(MSUtils.getScoreboardHideTags());
		player.displayName(playerInfo.createDefaultName());
		player.setGameMode(GameMode.SPECTATOR);

		event.joinMessage(null);

		if (player.isDead()) {
			playerInfo.teleportToDarkWorld();
		}

		new BukkitRunnable() {
			public void run() {
				if (!player.isOnline()) this.cancel();
				if (MSUtils.getAuthMeApi().isAuthenticated(player)) {
					if (!playerFile.exists() || (playerFile.exists() && playerFile.isNoName())) {
						this.cancel();
						new RegistrationProcess().registerPlayer(playerInfo);
					} else {
						this.cancel();
						if (playerFile.getYamlConfiguration().getString("pronouns") == null) {
							Pronouns.Menu.open(player);
						} else {
							if (playerFile.getPlayerSettings().getResourcePackType() == ResourcePack.Type.NONE) {
								playerInfo.teleportToLastLeaveLocation();
							} else {
								ResourcePack.setResourcePack(playerInfo);
							}
						}
					}
				}
			}
		}.runTaskTimer(MSUtils.getInstance(), 1L, 1L);
	}
}
