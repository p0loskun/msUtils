package com.github.minersstudios.msutils.listeners.inventory;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.player.CraftsMenu;
import com.github.minersstudios.msutils.player.Pronouns;
import com.github.minersstudios.msutils.player.ResourcePack;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class InventoryDragListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryDrag(@NotNull InventoryDragEvent event) {
		Component inventoryTitle = event.getView().title();
		if (
				inventoryTitle.contains(CraftsMenu.CRAFT_NAME)
				|| inventoryTitle.contains(CraftsMenu.CRAFTS_NAME)
				|| inventoryTitle.contains(CraftsMenu.CATEGORY_NAME)
				|| inventoryTitle.contains(ResourcePack.Menu.NAME)
				|| inventoryTitle.contains(Pronouns.NAME)
		) {
			event.setCancelled(true);
		}
	}
}
