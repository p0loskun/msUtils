package com.github.minersstudios.msUtils.listeners.player;

import com.github.minersstudios.msUtils.enums.ResourcePackType;
import com.github.minersstudios.msUtils.utils.ChatUtils;
import com.github.minersstudios.msUtils.Main;
import com.github.minersstudios.msUtils.classes.PlayerInfo;
import com.github.minersstudios.msUtils.classes.RegistrationProcess;
import com.github.minersstudios.msUtils.enums.Pronouns;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;

public class PlayerJoinListener implements Listener {

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId(), ChatUtils.legacyComponentSerialize(player.name()));

		event.joinMessage(null);
		Main.getScoreboardHideTagsTeam().addEntry(player.getName());
		player.setScoreboard(Main.getScoreboardHideTags());
		player.displayName(playerInfo.getDefaultName());
		player.setGameMode(GameMode.SPECTATOR);

		if (player.isDead()) {
			playerInfo.teleportToDarkWorld();
		}

		new BukkitRunnable() {
			public void run() {
				if (!player.isOnline()) this.cancel();
				if (Main.getAuthMeApi().isAuthenticated(player)) {
					if (!playerInfo.hasPlayerDataFile() || (playerInfo.hasPlayerDataFile() && playerInfo.hasNoName())) {
						this.cancel();
						new RegistrationProcess().registerPlayer(playerInfo);
					} else {
						this.cancel();
						if (playerInfo.getYamlConfiguration().getString("pronouns") == null) {
							player.openInventory(Pronouns.getInventory());
						} else {
							if (playerInfo.getResourcePackType() == ResourcePackType.NONE) {
								playerInfo.teleportToLastLeaveLocation();
							} else {
								ResourcePackType.setResourcePack(playerInfo);
							}
						}
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 1L, 1L);
	}
}
