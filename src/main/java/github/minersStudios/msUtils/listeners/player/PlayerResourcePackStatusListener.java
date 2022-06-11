package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.enums.DiskType;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import javax.annotation.Nonnull;

public class PlayerResourcePackStatusListener implements Listener {

    @EventHandler
    public void onPlayerResourcepackStatus(@Nonnull PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());

        if(playerInfo.getResourcePackType() != null) {
            if (event.getStatus().equals(PlayerResourcePackStatusEvent.Status.ACCEPTED)) {
                ChatUtils.sendFine(null, player.getName() + " принял ресурспак");
            }
            if (event.getStatus().equals(PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED)) {
                ChatUtils.sendFine(null, player.getName() + " успешно загрузил ресурспак");
                playerInfo.teleportToLastLeaveLocation();
            }
            if (event.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
                ChatUtils.sendWarning(null, player.getName() + " не установился ресурспак, диск : " + playerInfo.getDiskType());
                if(playerInfo.getDiskType() == DiskType.DROPBOX){
                    playerInfo.setDiskType(DiskType.YANDEX_DISK);
                    player.setResourcePack(playerInfo.getResourcePackType().getYandexDiskURL());
                } else {
                    playerInfo.setResourcePackType(ResourcePackType.NONE);
                    player.kickPlayer(
                            ChatColor.RED + "\n§lКажеться, что-то пошло не так"
                            + ChatColor.DARK_GRAY + "\n\n<---====+====--->"
                            + ChatColor.GRAY + "\nОбратитесь к администрации\nА пока ваш тип ресурспака изменён на :\n\"Без текстурпака\""
                            + ChatColor.DARK_GRAY + "\n<---====+====--->\n"
                    );
                }
            }
            if (event.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED)) {
                ChatUtils.sendWarning(null, player.getName() + " не принял ресурспак");
                player.kickPlayer(
                        ChatColor.RED + "\n§lКажеться, что-то пошло не так"
                                + ChatColor.DARK_GRAY + "\n\n<---====+====--->"
                                + ChatColor.GRAY + "\nВ настройках сервера поменяйте параметр :\n\"Наборы ресурсов\" на \"Включены\""
                                + ChatColor.DARK_GRAY + "\n<---====+====--->\n"
                );
            }
        }
    }
}
