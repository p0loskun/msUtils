package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.classes.SitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
        SitPlayer sitPlayer = new SitPlayer(player);
        event.setDeathMessage(
                playerInfo.getGoldenName() + " "
                + playerInfo.getPronouns().getDeathMessage()
        );

        if (sitPlayer.isSitting()) {
            sitPlayer.setSitting(null);
        }
        if (playerInfo.hasPlayerDataFile()) {
            playerInfo.setLastDeathLocation(player.getLocation());
        }
    }
}
