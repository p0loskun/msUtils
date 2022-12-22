package com.github.minersstudios.msutils.player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.github.minersstudios.msutils.Main;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public final class SignMenu {
	private static final Map<Player, SignMenu> inputs = new HashMap<>();
	private final List<String> text;
	private BiPredicate<Player, String[]> response;
	private Location location;

	public SignMenu(String @NotNull ... text) {
		this.text = List.of(text);

		Main.getProtocolManager().addPacketListener(new PacketAdapter(Main.getInstance(), PacketType.Play.Client.UPDATE_SIGN) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				Player player = event.getPlayer();
				SignMenu menu = inputs.remove(player);

				if (menu == null) return;

				event.setCancelled(true);
				menu.location.setY(200);

				if (!menu.response.test(player, event.getPacket().getStringArrays().read(0))) {
					Bukkit.getScheduler().runTaskLater(this.plugin, () -> menu.open(player), 2L);
				}
				Bukkit.getScheduler().runTask(this.plugin, () -> {
					if (player.isOnline()) {
						player.sendBlockChange(menu.location, menu.location.getBlock().getBlockData());
					}
				});
			}
		});
	}

	public SignMenu response(@NotNull BiPredicate<Player, String[]> response) {
		this.response = response;
		return this;
	}

	public void open(@NotNull Player player) {
		this.location = player.getLocation();
		this.location.setY(200);

		player.sendBlockChange(this.location, Material.OAK_SIGN.createBlockData());
		player.sendSignChange(this.location, Lists.newArrayList(
				Component.text(this.text.get(0)),
				Component.text(this.text.get(1)),
				Component.text(this.text.get(2)),
				Component.text(this.text.get(3)))
		);

		PacketContainer openSign = Main.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
		openSign.getBlockPositionModifier().write(0, new BlockPosition(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ()));
		try {
			Main.getProtocolManager().sendServerPacket(player, openSign);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		inputs.put(player, this);
	}
}
