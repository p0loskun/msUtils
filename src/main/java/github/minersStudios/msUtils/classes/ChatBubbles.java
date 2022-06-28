package github.minersStudios.msUtils.classes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ChatBubbles {
	public int receiveMessage(@Nonnull Player player, @Nonnull String chat) {
		String[] chatLines = chat.split("\n");
		new ArrayList<LivingEntity>();

		int duration = (chat.length() + (17 * chatLines.length)) * 1200 / 800;
		Location spawnPoint = player.getLocation();
		spawnPoint.setY(-1);

		Entity vehicle = player;
		for (int i = chatLines.length - 1 ; i >= 0 ; i--) {
			vehicle = spawnNameTag(vehicle, chatLines[i], spawnPoint, duration, i == 0);
		}
		return duration;
	}

	@Nonnull
	private static AreaEffectCloud spawnNameTag(@Nonnull Entity vehicle, @Nonnull String text, @Nonnull Location spawnPoint, int duration, boolean firstLine) {
		return spawnPoint.getBlock().getWorld().spawn(spawnPoint, AreaEffectCloud.class, (areaEffectCloud) -> {
			areaEffectCloud.setParticle(Particle.TOWN_AURA);
			areaEffectCloud.setRadius(0);

			vehicle.addPassenger(areaEffectCloud);
			areaEffectCloud.setCustomName(" \u00A7f" + (firstLine ? "\uA015 " : "") + text + " ");
			areaEffectCloud.setCustomNameVisible(true);

			areaEffectCloud.setWaitTime(0);
			areaEffectCloud.setDuration(duration);
		});
	}
}
