package com.github.minersstudios.msutils.listeners.entity;

import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class EntityDamageByEntityListener implements Listener {

	@EventHandler
	public void onRemoveItem(@NotNull EntityDamageByEntityEvent event){
		if(
				event.getEntity() instanceof ItemFrame itemFrame
				&& itemFrame.getScoreboardTags().contains("invisibleItemFrame")
				&& !itemFrame.isVisible()
		){
			itemFrame.setVisible(true);
		}
	}
}
