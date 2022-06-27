package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.classes.SitPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

public class PlayerDeathListener implements Listener {

	@EventHandler
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		Player killed = event.getEntity(), killer = event.getEntity().getKiller();
		PlayerInfo killedInfo = new PlayerInfo(killed.getUniqueId());
		SitPlayer sitPlayer = new SitPlayer(killed);
		if(killer != null){
			event.setDeathMessage(" " + new PlayerInfo(killer.getUniqueId()).getGoldenName() + ChatColor.of("#ffee93") + " убил " + killedInfo.getGoldenName());
		} else {
			event.setDeathMessage(" " + killedInfo.getGoldenName() + ChatColor.of("#ffee93") + " умер");
		}
		if (sitPlayer.isSitting()) {
			sitPlayer.setSitting(null);
		}
		if (killedInfo.hasPlayerDataFile()) {
			killedInfo.setLastDeathLocation(killed.getLocation());
		}
	}
}
