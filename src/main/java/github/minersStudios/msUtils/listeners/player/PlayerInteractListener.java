package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.annotation.Nonnull;

public class PlayerInteractListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		event.setCancelled(event.getPlayer().getWorld() == Main.worldDark);
	}
}
