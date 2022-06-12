package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.classes.SitPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.annotation.Nonnull;

public class PlayerTeleportListener implements Listener {

    @EventHandler
    public void onPlayerTeleport(@Nonnull PlayerTeleportEvent event){
        SitPlayer sitPlayer = new SitPlayer(event.getPlayer());
        if (sitPlayer.isSitting()) {
            sitPlayer.setSitting(false);
        }
    }
}
