package com.github.minersstudios.msUtils.commands.other;

import com.github.minersstudios.msUtils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import javax.annotation.Nonnull;

public class GetMapLocationCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		}
		ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
		if (!(itemInMainHand instanceof MapMeta mapMeta)) {
			return ChatUtils.sendWarning(player, Component.text("Возьмите в правую руку карту!"));
		}
		MapView mapView = mapMeta.getMapView();
		if (mapView == null || mapView.getWorld() == null) {
			return ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
		}
		return ChatUtils.sendWarning(player,
				Component.text("Мир карты : "
						+ "\n  " + ChatColor.WHITE + mapView.getWorld().getName()
						+ "\n ꀓ " + ChatColor.GOLD + "Координаты точки центра карты : "
						+ ChatColor.GREEN + "\n - X : " + ChatColor.WHITE + mapView.getCenterX()
						+ ChatColor.GREEN + "\n - Y : " + ChatColor.WHITE + "~"
						+ ChatColor.GREEN + "\n - Z : " + ChatColor.WHITE + mapView.getCenterZ())
		);
	}
}
