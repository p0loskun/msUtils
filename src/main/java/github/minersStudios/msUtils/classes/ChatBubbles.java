package github.minersStudios.msUtils.classes;

import github.minersStudios.msUtils.utils.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ChatBubbles {

	public int receiveMessage(@Nonnull Player player, @Nonnull String chat) {
		String[] chatLines = chat.split("\n");
		int duration = (chat.length() + (17 * chatLines.length)) * 1200 / 800;
		Entity vehicle = player;
		for (int i = chatLines.length - 1 ; i >= 0 ; i--)
			vehicle = spawnNameTag(vehicle, chatLines[i], player.getLocation().clone().add(0.0d, 1.0d, 0.0d), duration, i == 0);
		return duration;
	}

	@Nonnull
	private static AreaEffectCloud spawnNameTag(@Nonnull Entity vehicle, @Nonnull String text, @Nonnull Location spawnPoint, int duration, boolean firstLine) {
		return spawnPoint.getBlock().getWorld().spawn(spawnPoint, AreaEffectCloud.class, (areaEffectCloud) -> {
			areaEffectCloud.setParticle(Particle.TOWN_AURA);
			areaEffectCloud.setRadius(0);
			areaEffectCloud.customName(
					(firstLine ? Config.Symbols.speech : Component.text(""))
					.append(Component.text(text))
					.append(Component.text(" "))
					.color(NamedTextColor.WHITE)
			);
			areaEffectCloud.setCustomNameVisible(true);
			areaEffectCloud.setWaitTime(0);
			areaEffectCloud.setDuration(duration);
			vehicle.addPassenger(areaEffectCloud);
		});
	}
}
