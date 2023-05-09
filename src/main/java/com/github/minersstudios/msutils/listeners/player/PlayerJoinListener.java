package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.Pronouns;
import com.github.minersstudios.msutils.player.RegistrationProcess;
import com.github.minersstudios.msutils.player.ResourcePack;
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
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId(), ChatUtils.serializeLegacyComponent(player.name()));
		MSUtils.getScoreboardHideTagsTeam().addEntry(player.getName());
		player.setScoreboard(MSUtils.getScoreboardHideTags());
		player.displayName(playerInfo.getDefaultName());
		player.setGameMode(GameMode.SPECTATOR);

		event.joinMessage(null);

		if (player.isDead()) {
			playerInfo.teleportToDarkWorld();
		}

		new BukkitRunnable() {
			public void run() {
				if (!player.isOnline()) this.cancel();
				if (MSUtils.getAuthMeApi().isAuthenticated(player)) {
					if (!playerInfo.hasPlayerDataFile() || (playerInfo.hasPlayerDataFile() && playerInfo.hasNoName())) {
						this.cancel();
						new RegistrationProcess().registerPlayer(playerInfo);
					} else {
						this.cancel();
						if (playerInfo.getYamlConfiguration().getString("pronouns") == null) {
							Pronouns.Menu.open(player);
						} else {
							if (playerInfo.getResourcePackType() == ResourcePack.Type.NONE) {
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
