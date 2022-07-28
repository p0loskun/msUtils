package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import javax.annotation.Nonnull;

public class PlayerResourcePackStatusListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerResourcepackStatus(@Nonnull PlayerResourcePackStatusEvent event) {
		Player player = event.getPlayer();
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.getResourcePackType() == null) return;
		switch (event.getStatus()) {
			case ACCEPTED -> ChatUtils.sendFine(null, player.getName() + " принял ресурспак");
			case SUCCESSFULLY_LOADED -> {
				ChatUtils.sendFine(null, player.getName() + " успешно загрузил ресурспак");
				if (player.getWorld() == Main.worldDark)
					playerInfo.teleportToLastLeaveLocation();
			}
			case FAILED_DOWNLOAD -> {
				ChatUtils.sendWarning(null, player.getName() + " не установился ресурспак, диск : " + playerInfo.getDiskType());
				if (playerInfo.getDiskType() == ResourcePackType.DiskType.DROPBOX) {
					playerInfo.setDiskType(ResourcePackType.DiskType.YANDEX_DISK);
					player.setResourcePack(playerInfo.getResourcePackType().getYandexDiskURL());
				} else {
					playerInfo.setDiskType(null);
					playerInfo.setResourcePackType(ResourcePackType.NONE);
					PlayerUtils.kickPlayer(player, "Кажеться, что-то пошло не так", "Обратитесь к администрации\nА пока ваш тип ресурспака изменён на :\n \"Без текстурпака\"");
				}
			}
			case DECLINED -> {
				ChatUtils.sendWarning(null, player.getName() + " не принял ресурспак");
				PlayerUtils.kickPlayer(player, "Кажеться, что-то пошло не так", "В настройках сервера поменяйте параметр :\n\"Наборы ресурсов\" на \"Включены\"");
			}
		}
	}
}