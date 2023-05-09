package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

@MSCommand(
		command = "getmaplocation",
		aliases = {"getmaploc"},
		usage = " ꀑ §cИспользуй: /<command>",
		description = "Добывает координаты карты, находящейся в руке",
		permission = "msutils.maplocation",
		permissionDefault = PermissionDefault.OP
)
public class GetMapLocationCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
			return true;
		}
		if (!(player.getInventory().getItemInMainHand().getItemMeta() instanceof MapMeta mapMeta)) {
			ChatUtils.sendWarning(player, Component.text("Возьмите в правую руку карту!"));
			return true;
		}
		MapView mapView = mapMeta.getMapView();
		if (mapView == null || mapView.getWorld() == null) {
			ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
			return true;
		}
		int x = mapView.getCenterX();
		int z = mapView.getCenterZ();
		int y = mapView.getWorld().getHighestBlockYAt(x, z) + 1;
		ChatUtils.sendWarning(player,
				Component.text("Мир карты : ")
				.append(Component.text(mapView.getWorld().getName(), NamedTextColor.WHITE))
				.append(Component.text("\n ꀓ ", NamedTextColor.WHITE))
				.append(Component.text("Координаты точки центра карты : ", NamedTextColor.GOLD))
				.append(Component.text("\n   - X : ", NamedTextColor.GREEN))
				.append(Component.text(x, NamedTextColor.WHITE))
				.append(Component.text("\n   - Y : ", NamedTextColor.GREEN))
				.append(Component.text(y, NamedTextColor.WHITE))
				.append(Component.text("\n   - Z : ", NamedTextColor.GREEN))
				.append(Component.text(z, NamedTextColor.WHITE))
				.append(Component.text("\n\n  Телепортироваться - ЖМЯК\n", Style.style(TextDecoration.BOLD))
						.clickEvent(ClickEvent.runCommand(
								"/tp " + x + " " + y + " " + z
						))
				)
		);
		return true;
	}
}
