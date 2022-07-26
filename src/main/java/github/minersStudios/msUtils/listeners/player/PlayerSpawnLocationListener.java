package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import javax.annotation.Nonnull;

public class PlayerSpawnLocationListener implements Listener {

    @EventHandler
    public void onPlayerSpawnLocation(@Nonnull PlayerSpawnLocationEvent event) {
        if (!event.getPlayer().isDead())
            event.setSpawnLocation(new Location(Main.worldDark,  0.0d, 0.0d, 0.0d));
    }
}
