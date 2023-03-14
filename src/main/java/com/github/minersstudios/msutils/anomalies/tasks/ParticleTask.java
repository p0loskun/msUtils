package com.github.minersstudios.msutils.anomalies.tasks;

import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.anomalies.Anomaly;
import com.github.minersstudios.msutils.anomalies.AnomalyAction;
import com.github.minersstudios.msutils.anomalies.actions.SpawnParticlesAction;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import org.bukkit.Bukkit;

public class ParticleTask implements Runnable {

	@Override
	public void run() {
		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), () ->
				Bukkit.getOnlinePlayers().stream()
				.filter(PlayerUtils::isOnline)
				.forEach((player) -> {
					for (Anomaly anomaly : MSUtils.getConfigCache().anomalies.values()) {
						Double radiusInside = anomaly.getBoundingBox().getRadiusInside(player);
						if (radiusInside != null) {
							for (AnomalyAction action : anomaly.getAnomalyActionMap().get(radiusInside)) {
								if (action instanceof SpawnParticlesAction) {
									action.doAction(player, null);
								}
							}
						}
					}
				})
		);
	}
}
