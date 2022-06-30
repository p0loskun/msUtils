package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.classes.SitPlayer;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		SitPlayer sitPlayer = new SitPlayer(player);
		if (sitPlayer.isSitting())
			sitPlayer.setSitting(null);
		event.setQuitMessage(null);
		if (playerInfo.hasPlayerDataFile() && player.getWorld() != Main.worldDark) {
			playerInfo.setLastLeaveLocation(player);
			if (playerInfo.hasName())
				ChatUtils.sendLeaveMessage(playerInfo, player);
		}
	}
}
