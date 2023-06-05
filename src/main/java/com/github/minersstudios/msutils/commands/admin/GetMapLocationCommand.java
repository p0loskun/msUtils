package com.github.minersstudios.msutils.commands.admin;

import com.github.minersstudios.mscore.command.MSCommand;
import com.github.minersstudios.mscore.command.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
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
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.Component.text;

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
	public boolean onCommand(
			@NotNull CommandSender sender,
			@NotNull Command command,
			@NotNull String label,
			String @NotNull ... args
	) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, "Только игрок может использовать эту команду!");
			return true;
		}
		if (!(player.getInventory().getItemInMainHand().getItemMeta() instanceof MapMeta mapMeta)) {
			ChatUtils.sendWarning(player, "Возьмите карту в правую руку!");
			return true;
		}
		MapView mapView = mapMeta.getMapView();
		if (mapView == null || mapView.getWorld() == null) {
			ChatUtils.sendError(sender, "Кажется, что-то пошло не так...");
			return true;
		}
		int x = mapView.getCenterX();
		int z = mapView.getCenterZ();
		int y = mapView.getWorld().getHighestBlockYAt(x, z) + 1;
		ChatUtils.sendWarning(player,
				text("Мир карты : ")
				.append(text(mapView.getWorld().getName(), NamedTextColor.WHITE))
				.append(text("\n ꀓ ", NamedTextColor.WHITE))
				.append(text("Координаты точки центра карты : ", NamedTextColor.GOLD))
				.append(text("\n   - X : ", NamedTextColor.GREEN))
				.append(text(x, NamedTextColor.WHITE))
				.append(text("\n   - Y : ", NamedTextColor.GREEN))
				.append(text(y, NamedTextColor.WHITE))
				.append(text("\n   - Z : ", NamedTextColor.GREEN))
				.append(text(z, NamedTextColor.WHITE))
				.append(text("\n\n  Телепортироваться - ЖМЯК\n", Style.style(TextDecoration.BOLD))
						.clickEvent(ClickEvent.runCommand(
								"/tp " + x + " " + y + " " + z
						))
				)
		);
		return true;
	}

	@Override
	public @Nullable CommandNode<?> getCommandNode() {
		return LiteralArgumentBuilder.literal("getmaplocation").build();
	}
}
