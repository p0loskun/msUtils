package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

public class PlayerDeathListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		event.deathMessage(null);
		Player killed = event.getEntity();
		PlayerUtils.setSitting(killed, null);
		ChatUtils.sendDeathMessage(killed, killed.getKiller());
	}
}
