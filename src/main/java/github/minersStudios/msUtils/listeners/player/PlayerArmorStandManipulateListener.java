package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.utils.EventListener;
import github.minersStudios.msUtils.utils.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import javax.annotation.Nonnull;

@EventListener
public class PlayerArmorStandManipulateListener implements Listener {

	@EventHandler
	public void onPlayerArmorStandManipulate(@Nonnull PlayerArmorStandManipulateEvent event) {
		event.setCancelled(PlayerUtils.getSeats().containsValue(event.getRightClicked()));
	}
}
