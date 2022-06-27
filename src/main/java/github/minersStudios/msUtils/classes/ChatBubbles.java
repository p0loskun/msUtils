package github.minersStudios.msUtils.classes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;

import java.util.ArrayList;

public class ChatBubbles {

	int receiveMessage(Player player, String chat) {
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

	private AreaEffectCloud spawnNameTag(Entity vehicle, String text, Location spawnPoint, int duration, boolean firstLine) {
		AreaEffectCloud nameTag = (AreaEffectCloud) spawnPoint.getBlock().getWorld().spawnEntity(spawnPoint, EntityType.AREA_EFFECT_CLOUD);
		nameTag.setParticle(Particle.TOWN_AURA);
		nameTag.setRadius(0);

		vehicle.addPassenger(nameTag);
		nameTag.setCustomName(" \u00A7f" + (firstLine ? "\uA015 " : "") + text + " ");
		nameTag.setCustomNameVisible(true);

		nameTag.setWaitTime(0);
		nameTag.setDuration(duration);
		return nameTag;
	}
}
