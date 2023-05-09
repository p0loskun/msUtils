package com.github.minersstudios.msutils.anomalies;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;

@SuppressWarnings("unused")
public abstract class AnomalyAction {
	protected static final SecureRandom random = new SecureRandom();

	protected final long time;
	protected final int percentage;

	protected AnomalyAction(
			long time,
			int percentage
	) {
		this.time = time;
		this.percentage = percentage;
	}

	public abstract void doAction(@NotNull Player player, @Nullable AnomalyIgnorableItems ignorableItems);

	public void removeAction(@NotNull Player player) {
		if (getConfigCache().playerAnomalyActionMap.get(player).size() > 1) {
			Map<AnomalyAction, Long> actions = new HashMap<>(getConfigCache().playerAnomalyActionMap.get(player));
			actions.remove(this);
			getConfigCache().playerAnomalyActionMap.put(player, actions);
		} else {
			getConfigCache().playerAnomalyActionMap.remove(player);
		}
	}

	public boolean isDo() {
		return random.nextInt(100) < this.percentage;
	}

	public long getTime() {
		return this.time;
	}

	public int getPercentage() {
		return this.percentage;
	}
}
