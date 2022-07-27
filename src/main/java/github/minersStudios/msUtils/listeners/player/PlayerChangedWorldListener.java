package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.utils.EventListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import javax.annotation.Nonnull;

@EventListener
public class PlayerChangedWorldListener implements Listener {

	@EventHandler
	public void onPlayerChangedWorld(@Nonnull PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		Main.scoreboardHideTagsTeam.addEntry(player.getName());
		player.setScoreboard(Main.scoreboardHideTags);
	}
}
