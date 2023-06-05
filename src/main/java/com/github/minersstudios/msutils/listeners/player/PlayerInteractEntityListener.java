package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.listener.MSListener;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import com.github.minersstudios.msutils.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.*;

@MSListener
public class PlayerInteractEntityListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteractEntity(@NotNull PlayerInteractEntityEvent event) {
		Player playerWhoClicked = event.getPlayer();
		if (event.getRightClicked() instanceof Player clickedPlayer) {
			float pitch = playerWhoClicked.getEyeLocation().getPitch();
			if (
					(pitch >= 80 && pitch <= 90)
					&& playerWhoClicked.isSneaking()
					&& !playerWhoClicked.getPassengers().isEmpty()
			) {
				playerWhoClicked.eject();
			}

			PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(clickedPlayer);
			playerWhoClicked.sendActionBar(
					empty()
					.append(playerInfo.getGoldenName())
					.append(space())
					.append(text(playerInfo.getPlayerFile().getPlayerName().getPatronymic(), MessageUtils.Colors.JOIN_MESSAGE_COLOR_PRIMARY))
			);
			ItemStack helmet = clickedPlayer.getInventory().getHelmet();
			if (
					!playerWhoClicked.isInsideVehicle()
					&& helmet != null
					&& !playerWhoClicked.isSneaking()
					&& helmet.getType() == Material.SADDLE
			) {
				List<Entity> passengers = clickedPlayer.getPassengers();
				if (passengers.isEmpty()) {
					clickedPlayer.addPassenger(playerWhoClicked);
				} else {
					passengers.get(passengers.size() - 1).addPassenger(playerWhoClicked);
				}
			}
		} else if (event.getRightClicked() instanceof ItemFrame itemFrame) {
			Material itemInItemFrameMaterial = itemFrame.getItem().getType(),
					itemInMainHandMaterial = playerWhoClicked.getInventory().getItemInMainHand().getType();
			if (
					itemInItemFrameMaterial.isAir()
					&& !itemInMainHandMaterial.isAir()
					&& itemFrame.getScoreboardTags().contains("invisibleItemFrame")
			) {
				itemFrame.setVisible(false);
			} else if (
					(!itemInItemFrameMaterial.isAir() || playerWhoClicked.isSneaking())
					&& itemInMainHandMaterial == Material.SHEARS
					&& !itemFrame.getScoreboardTags().contains("invisibleItemFrame")
			) {
				playerWhoClicked.getWorld().playSound(itemFrame.getLocation(), Sound.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0f, 1.0f);
				itemFrame.addScoreboardTag("invisibleItemFrame");
				itemFrame.setVisible(itemInItemFrameMaterial.isAir());
				event.setCancelled(true);
			}
		}
	}
}
