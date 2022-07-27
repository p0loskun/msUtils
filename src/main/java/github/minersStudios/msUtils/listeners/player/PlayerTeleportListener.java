package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.SitPlayer;
import github.minersStudios.msUtils.utils.EventListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.annotation.Nonnull;

@EventListener
public class PlayerTeleportListener implements Listener {

	@EventHandler
	public void onPlayerTeleport(@Nonnull PlayerTeleportEvent event) {
		SitPlayer sitPlayer = new SitPlayer(event.getPlayer());
		if (sitPlayer.isSitting())
			sitPlayer.setSitting(null);
		event.setCancelled(event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE && event.getPlayer().getWorld() == Main.worldDark);
	}
}
