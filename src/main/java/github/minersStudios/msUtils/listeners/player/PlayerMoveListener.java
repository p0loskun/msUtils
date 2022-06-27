package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;

public class PlayerMoveListener implements Listener {

	@EventHandler
	public void onPlayerMove(@Nonnull PlayerMoveEvent event){
		Player player = event.getPlayer();
		event.setCancelled(player.getWorld() == Main.worldDark);
		player.setGravity(player.getWorld() != Main.worldDark && player.hasGravity());
	}
}
