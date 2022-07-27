package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.classes.SitPlayer;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.EventListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

@EventListener
public class PlayerDeathListener implements Listener {

	@EventHandler
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		event.setDeathMessage(null);
		Player killed = event.getEntity();

		SitPlayer sitPlayer = new SitPlayer(killed);
		if (sitPlayer.isSitting())
			sitPlayer.setSitting(null);

		ChatUtils.sendDeathMessage(killed, event.getEntity().getKiller());
	}
}
