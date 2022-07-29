package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.utils.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.annotation.Nonnull;

public class PlayerTeleportListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerTeleport(@Nonnull PlayerTeleportEvent event) {
		PlayerUtils.setSitting(event.getPlayer(), null);
		event.setCancelled(event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE && event.getPlayer().getWorld() == Main.getWorldDark());
	}
}