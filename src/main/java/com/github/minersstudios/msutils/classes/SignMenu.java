package com.github.minersstudios.msutils.classes;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.github.minersstudios.msutils.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiPredicate;

public class SignMenu {
	private static final Map<Player, SignMenu> inputs = new HashMap<>();
	private final List<String> text;
	private BiPredicate<Player, String[]> response;
	private final Location location = new Location(Main.getWorldDark(), 0.0d, 200.0d, 0.0d);

	public SignMenu(List<String> text) {
		this.text = text;

		Main.getProtocolManager().addPacketListener(new PacketAdapter(Main.getInstance(), PacketType.Play.Client.UPDATE_SIGN) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				Player player = event.getPlayer();
				SignMenu menu = inputs.remove(player);
				if (menu == null) return;
				event.setCancelled(true);
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

	public SignMenu response(@Nonnull BiPredicate<Player, String[]> response) {
		this.response = response;
		return this;
	}

	public void open(@Nonnull Player player) {
		PacketContainer packet = Main.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
		packet.getBlockPositionModifier().write(0, new BlockPosition(this.location.toVector()));

		player.sendBlockChange(this.location, Material.OAK_SIGN.createBlockData());
		player.sendSignChange(this.location, Arrays.asList(
				Component.text(this.text.get(0)),
				Component.text(this.text.get(1)),
				Component.text(this.text.get(2)),
				Component.text(this.text.get(3)))
		);

		try {
			Main.getProtocolManager().sendServerPacket(player, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		inputs.put(player, this);
	}
}
