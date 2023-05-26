package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
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

@MSListener
public class PlayerInteractEntityListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteractEntity(@NotNull PlayerInteractEntityEvent event) {
		Player playerWhoClicked = event.getPlayer();
		if (event.getRightClicked() instanceof Player clickedPlayer) {
			PlayerInfo playerInfo = new PlayerInfo(clickedPlayer.getUniqueId());
			playerWhoClicked.sendActionBar(
					Component.empty()
					.append(playerInfo.createGoldenName())
					.append(Component.space())
					.append(Component.text(playerInfo.getPlayerFile().getPlayerName().getPatronymic()))
					.color(ChatUtils.Colors.JOIN_MESSAGE_COLOR_PRIMARY)
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
