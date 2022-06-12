package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.classes.RegistrationProcess;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(@Nonnull PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());

        Main.scoreboardHideTagsTeam.addEntry(player.getName());
        player.setScoreboard(Main.scoreboardHideTags);
        player.setDisplayName("[" + playerInfo.getID() + "] " + playerInfo.getFirstname() + " " + playerInfo.getLastname());

        if(playerInfo.getIP() != null && !Objects.equals(playerInfo.getIP(), Objects.requireNonNull(player.getAddress()).getHostName())){
            ChatUtils.sendWarning(null,
                    "Игрок : \"" + ChatColor.GRAY + "[" + playerInfo.getID() + "] " + ChatColor.GOLD + playerInfo.getFirstname() + " " + playerInfo.getLastname()
                    + "\" сменил свой айпи с : " + playerInfo.getIP()
                    + "\n На :" + player.getAddress().getHostName()
            );
            playerInfo.setIP(player.getAddress().getHostName());
        }

        if(
                (playerInfo.isBanned() && playerInfo.getBannedTo() - System.currentTimeMillis() < 0)
                || (playerInfo.isBanned() && !Bukkit.getBanList(BanList.Type.NAME).isBanned(player.getName()))
        ){
            playerInfo.setBanned(false, 0, null, null);
        }

        event.setJoinMessage(null);
        playerInfo.teleportToDarkWorld();

        new BukkitRunnable() {
            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                }
                if (Main.authmeApi.isAuthenticated(player)) {
                    if (!playerInfo.hasPlayerDataFile() || (playerInfo.hasPlayerDataFile() && !playerInfo.hasName())) {
                        this.cancel();
                        new RegistrationProcess().registerPlayer(playerInfo);
                    } else {
                        this.cancel();
                        player.setDisplayName("[" + playerInfo.getID() + "] " + playerInfo.getFirstname() + " " + playerInfo.getLastname());
                        if (playerInfo.getResourcePackType() == ResourcePackType.NONE){
                            playerInfo.teleportToLastLeaveLocation();
                        } else {
                            ResourcePackType.setResourcePack(playerInfo);
                        }
                    }
                }
            }
        }.runTaskTimer(Main.plugin, 1L, 1L);
    }
}
