package com.github.minersstudios.msutils.anomalies.tasks;

import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.anomalies.Anomaly;
import com.github.minersstudios.msutils.anomalies.AnomalyAction;
import com.github.minersstudios.msutils.anomalies.actions.SpawnParticlesAction;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;

public class MainAnomalyActionsTask implements Runnable {

	@Override
	public void run() {
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		if (onlinePlayers.isEmpty()) return;
		Map<Player, Map<AnomalyAction, Long>> playerActionMap = MSUtils.getConfigCache().playerAnomalyActionMap;
		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(), () ->
				onlinePlayers.stream()
				.filter(PlayerUtils::isOnline)
				.forEach((player) -> {
					for (Anomaly anomaly : getConfigCache().anomalies.values()) {
						Double radiusInside = anomaly.getBoundingBox().getRadiusInside(player);
						if (radiusInside == null) continue;
						Map<AnomalyAction, Long> actionMap = new HashMap<>(playerActionMap.getOrDefault(player, Collections.emptyMap()));

						for (AnomalyAction action : anomaly.getAnomalyActionMap().get(radiusInside)) {
							actionMap.put(action, System.currentTimeMillis());
							playerActionMap.put(player, actionMap);
						}

						if (anomaly.getIgnorablePlayers().contains(player) || actionMap.isEmpty()) continue;
						for (AnomalyAction action : actionMap.keySet()) {
							if (action instanceof SpawnParticlesAction) continue;
							if (anomaly.isAnomalyActionRadius(action, radiusInside)) {
								action.doAction(player, anomaly.getIgnorableItems());
								return;
							} else {
								action.removeAction(player);
							}
						}
					}
					playerActionMap.remove(player);
				})
		);
	}
}
