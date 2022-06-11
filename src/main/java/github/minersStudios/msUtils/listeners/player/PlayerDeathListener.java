package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.classes.PlayerInfo;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(@Nonnull PlayerDeathEvent event){
        Player player = event.getEntity();
        PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
        if(playerInfo.hasPlayerDataFile()){
            Location playerLocation = player.getLocation();
            playerInfo.setLastDeathLocation(player.getWorld(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
        }
    }
}
