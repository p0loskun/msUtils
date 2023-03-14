package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msutils.player.PlayerInfo;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@MSListener
public class PlayerAdvancementDoneListener implements Listener {

	@EventHandler
	public void onPlayerAdvancementDone(@NotNull PlayerAdvancementDoneEvent event) {
		AdvancementDisplay advancementDisplay = event.getAdvancement().getDisplay();
		if (advancementDisplay == null || event.message() == null) return;
		AdvancementDisplay.Frame frame = advancementDisplay.frame();
		event.message(
				Component.space()
				.append(Component.translatable(
				"chat.type.advancement." + frame.name().toLowerCase(Locale.ROOT),
				new PlayerInfo(event.getPlayer().getUniqueId()).getDefaultName(),
				Component.text("[")
				.append(advancementDisplay.title())
				.append(Component.text("]"))
				.color(frame.color())
				.hoverEvent(HoverEvent.showText(
						advancementDisplay.title()
						.append(Component.newline().append(advancementDisplay.description()))
						.color(frame.color())
				))
				).color(NamedTextColor.GRAY))
		);
	}
}
