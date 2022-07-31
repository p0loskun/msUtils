package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;

public class PlayerInteractEntityListener implements Listener {

	@EventHandler
	public void onPlayerInteractEntity(@Nonnull PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Player player) {
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			event.getPlayer().sendActionBar(
					Component.text()
					.append(playerInfo.getGoldenName())
					.append(Component.text(" "))
					.append(Component.text(playerInfo.getPatronymic()))
					.color(Config.Colors.joinMessageColorPrimary)
					.build()
			);
		}
	}
}
