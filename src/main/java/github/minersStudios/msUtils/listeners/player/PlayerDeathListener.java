package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
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
		PlayerUtils.setSitting(killedPlayer, null);
		ChatUtils.sendDeathMessage(killedPlayer, killedPlayer.getKiller());
	}
}
