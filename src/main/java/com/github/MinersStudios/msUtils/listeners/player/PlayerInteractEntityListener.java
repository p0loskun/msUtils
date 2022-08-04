package com.github.MinersStudios.msUtils.listeners.player;

import com.github.MinersStudios.msUtils.classes.PlayerInfo;
import com.github.MinersStudios.msUtils.utils.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class PlayerInteractEntityListener implements Listener {

	@EventHandler
	public void onPlayerInteractEntity(@Nonnull PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Player clickedPlayer) {
			Player playerWhoClicked = event.getPlayer();
			PlayerInfo playerInfo = new PlayerInfo(clickedPlayer.getUniqueId());
			playerWhoClicked.sendActionBar(
					Component.text()
							.append(playerInfo.getGoldenName())
							.append(Component.text(" "))
							.append(Component.text(playerInfo.getPatronymic()))
							.color(Config.Colors.joinMessageColorPrimary)
							.build()
			);
			ItemStack helmet = clickedPlayer.getInventory().getHelmet();
			if (
					!playerWhoClicked.isInsideVehicle()
					&& helmet != null
					&& helmet.getType() == Material.SADDLE
			) {
				List<Entity> passengers = clickedPlayer.getPassengers();
				if (passengers.isEmpty()) {
					clickedPlayer.addPassenger(playerWhoClicked);
				} else {
					passengers.get(passengers.size() - 1).addPassenger(playerWhoClicked);
				}
			}
		}
	}
}
