package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import javax.annotation.Nonnull;

public class PlayerResourcePackStatusListener implements Listener {

	@EventHandler
	public void onPlayerResourcepackStatus(@Nonnull PlayerResourcePackStatusEvent event) {
		Player player = event.getPlayer();
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.getResourcePackType() == null) return;
		switch (event.getStatus()) {
			case ACCEPTED -> ChatUtils.sendFine(null, Component.text(player.getName()).append(Component.text(" принял ресурспак")));
			case SUCCESSFULLY_LOADED -> {
				ChatUtils.sendFine(null, Component.text(player.getName()).append(Component.text(" успешно загрузил ресурспак")));
				if (player.getWorld() == Main.getWorldDark())
					playerInfo.teleportToLastLeaveLocation();
			}
			case FAILED_DOWNLOAD -> {
				ChatUtils.sendWarning(null, Component.text(player.getName()).append(Component.text(" не установился ресурспак, диск : ")).append(Component.text(playerInfo.getDiskType().name())));
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
				ChatUtils.sendWarning(null, Component.text(player.getName()).append(Component.text(" не принял ресурспак")));
				PlayerUtils.kickPlayer(player, "Кажеться, что-то пошло не так", "В настройках сервера поменяйте параметр :\n\"Наборы ресурсов\" на \"Включены\"");
			}
		}
	}
}
