package com.github.minersstudios.msutils.listeners.entity;

import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class HangingBreakByEntityListener implements Listener {

	@EventHandler
	public void onRemoveFrame(@NotNull HangingBreakByEntityEvent event){
		if(
				event.getEntity() instanceof ItemFrame itemFrame
				&& itemFrame.getScoreboardTags().contains("invisibleItemFrame")
				&& itemFrame.isVisible()
		){
			itemFrame.removeScoreboardTag("invisibleItemFrame");
		}
	}
}
