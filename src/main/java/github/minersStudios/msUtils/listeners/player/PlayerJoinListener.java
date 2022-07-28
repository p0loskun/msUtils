package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.classes.RegistrationProcess;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;

public class PlayerJoinListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId(), player.getName());

		event.setJoinMessage(null);
		Main.scoreboardHideTagsTeam.addEntry(player.getName());
		player.setScoreboard(Main.scoreboardHideTags);
		player.setDisplayName(playerInfo.getDefaultName());
		player.setGameMode(GameMode.SPECTATOR);
		if (player.isDead())
			playerInfo.teleportToDarkWorld();

		if (
				playerInfo.hasPlayerDataFile()
				&& playerInfo.getIP() != null
				&& player.getAddress() != null
				&& !playerInfo.getIP().contains(player.getAddress().getHostName())
		) {
			ChatUtils.sendWarning(null,
					"Игроку : \"" + playerInfo.getGrayIDGoldName() + " "
					+ "\" был добавлен новый айпи адресс : " + player.getAddress().getHostName()
			);
			playerInfo.setIP(player.getAddress().getHostName());
		}

		if (
				(playerInfo.isBanned() && playerInfo.getBannedTo() - System.currentTimeMillis() < 0)
				|| (playerInfo.isBanned() && !Bukkit.getBanList(BanList.Type.NAME).isBanned(player.getName()))
		) playerInfo.setBanned(false, null);

		new BukkitRunnable() {
			public void run() {
				if (!player.isOnline())
					this.cancel();
				if (Main.authmeApi.isAuthenticated(player)) {
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
		}.runTaskTimer(Main.plugin, 1L, 1L);
	}
}