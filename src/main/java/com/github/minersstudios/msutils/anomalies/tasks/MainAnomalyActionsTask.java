package com.github.minersstudios.msutils.anomalies.tasks;

import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.anomalies.Anomaly;
import com.github.minersstudios.msutils.anomalies.AnomalyAction;
import com.github.minersstudios.msutils.anomalies.actions.SpawnParticlesAction;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import org.bukkit.Bukkit;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;

public class MainAnomalyActionsTask implements Runnable {

	@Override
	public void run() {
		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), () ->
				Bukkit.getOnlinePlayers().stream()
				.filter(PlayerUtils::isOnline)
				.forEach((player) -> {
					for (Anomaly anomaly : getConfigCache().anomalies.values()) {
						if (!anomaly.getIgnorablePlayers().contains(player)) {
							Double radiusInside = anomaly.getBoundingBox().getRadiusInside(player);
							if (radiusInside != null) {
								if (getConfigCache().playerAnomalyActionMap.containsKey(player)) {
									for (AnomalyAction action : getConfigCache().playerAnomalyActionMap.get(player).keySet()) {
										if (anomaly.isAnomalyActionRadius(action, radiusInside)) {
											action.doAction(player, anomaly.getIgnorableItems());
											return;
										} else {
											action.removeAction(player);
										}
									}
								} else {
									for (AnomalyAction action : anomaly.getAnomalyActionMap().get(radiusInside)) {
										if (!(action instanceof SpawnParticlesAction)) {
											action.doAction(player, anomaly.getIgnorableItems());
										}
									}
									return;
								}
							}
						}
					}
					getConfigCache().playerAnomalyActionMap.remove(player);
				})
		);
	}
}
