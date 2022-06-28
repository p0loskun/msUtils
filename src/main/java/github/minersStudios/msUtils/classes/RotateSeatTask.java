package github.minersStudios.msUtils.classes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import github.minersStudios.msUtils.Main;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RotateSeatTask extends BukkitRunnable {
	private RotateSeatTask.AlignArmorStand alignArmorStand;

	public RotateSeatTask() {
		try {
			this.alignArmorStand = new RotateSeatTask.AlignArmorStand() {
				final Method method;
				{this.method = Entity.class.getMethod("setRotation", Float.TYPE, Float.TYPE);}

				public void align(ArmorStand armorStand, float yaw) {
					try
						this.method.invoke(armorStand, yaw, 0);
					catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException exception)
						exception.printStackTrace();
				}
			};
		} catch (SecurityException | NoSuchMethodException exception) {
			this.alignArmorStand = (armorStand, yaw) -> {
				try {
					Object entityArmorStand = armorStand.getClass().getMethod("getHandle").invoke(armorStand);
					Field yawField = entityArmorStand.getClass().getField("yaw");
					yawField.set(entityArmorStand, yaw);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchFieldException | NoSuchMethodException exception1)
					exception1.printStackTrace();
			};
		}
		this.runTaskTimerAsynchronously(Main.plugin, 0L, 1L);
	}

	public void run() {
		for (ArmorStand armorstand : Main.plugin.getSeats().values()) {
			for (Object passenger : armorstand.getPassengers())
				if (passenger instanceof Player player) this.alignArmorStand.align(armorstand, player.getLocation().getYaw());
		}
	}

	private interface AlignArmorStand
		void align(ArmorStand armorStand, float value);
}
