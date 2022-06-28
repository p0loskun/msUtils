package github.minersStudios.msUtils.classes;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import github.minersStudios.msUtils.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public final class SignMenu {
	private static final Map<Player, SignMenu> inputs = new HashMap<>();
	private final List<String> text;
	private BiPredicate<Player, String[]> response;
	private Location location;

	public SignMenu(List<String> text) {
		this.text = text;

		Main.protocolManager.addPacketListener(new PacketAdapter(Main.plugin, PacketType.Play.Client.UPDATE_SIGN) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				Player player = event.getPlayer();
				SignMenu menu = inputs.remove(player);

				assert menu != null;
				event.setCancelled(true);
				menu.location.setY(200);

				if (!menu.response.test(player, event.getPacket().getStringArrays().read(0))) {
					Bukkit.getScheduler().runTaskLater(this.plugin, () -> menu.open(player), 2L);
				}
				Bukkit.getScheduler().runTask(this.plugin, () -> {
					if (player.isOnline())
						player.sendBlockChange(menu.location, menu.location.getBlock().getBlockData());
				});
			}
		});
	}

	public SignMenu response(@Nonnull BiPredicate<Player, String[]> response) {
		this.response = response;
		return this;
	}

	public void open(@Nonnull Player player) {
		assert this.player.isOnline != null;
		this.location = player.getLocation();
		this.location.setY(200);

		player.sendBlockChange(this.location, Material.OAK_SIGN.createBlockData());
		player.sendSignChange(this.location, this.text.toArray(new String[4]));

		PacketContainer openSign = Main.protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
		BlockPosition position = new BlockPosition(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ());
		openSign.getBlockPositionModifier().write(0, position);
		try {
			Main.protocolManager.sendServerPacket(player, openSign);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		inputs.put(player, this);
	}
}
