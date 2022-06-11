package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import javax.annotation.Nonnull;

public class PlayerDropItemListener implements Listener {

    @EventHandler
    public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if(player.getWorld() == Main.worldDark) {
            event.setCancelled(true);
            player.updateInventory();
        }
    }
}
