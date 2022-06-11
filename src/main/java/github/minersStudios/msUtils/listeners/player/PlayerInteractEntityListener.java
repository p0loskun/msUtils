package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.classes.PlayerInfo;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;

public class PlayerInteractEntityListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(@Nonnull PlayerInteractEntityEvent event){
        if(event.getRightClicked() instanceof Player player){
            PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.of("#fcf5c7") + "[" + playerInfo.getID() + "] " + ChatColor.of("#ffee93") + playerInfo.getFirstname() + " " + playerInfo.getLastname() + " " + playerInfo.getPatronymic()));
        }
    }
}
