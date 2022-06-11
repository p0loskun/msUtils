package github.minersStudios.msUtils.classes;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import github.minersStudios.msUtils.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;

public final class SignMenu {
    private final Map<Player, SignMenu> inputs = new HashMap<>();
    private final List<String> text;
    private BiPredicate<Player, String[]> response;
    private boolean reopenIfFail;
    private Location location;

    public SignMenu(List<String> text) {
        this.text = text;

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Main.plugin, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                SignMenu menu = inputs.remove(player);

                if (menu == null) return;
                event.setCancelled(true);
                menu.location.setY(200);

                if (!menu.response.test(player, event.getPacket().getStringArrays().read(0)) && menu.reopenIfFail) {
                    this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> menu.open(player), 2L);
                }
                this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
                    if (player.isOnline()) {
                        player.sendBlockChange(menu.location, menu.location.getBlock().getBlockData());
                    }
                });
            }
        });
    }

    public SignMenu reopenIfFail(boolean value) {
        this.reopenIfFail = value;
        return this;
    }

    public SignMenu response(BiPredicate<Player, String[]> response) {
        this.response = response;
        return this;
    }

    public void open(Player player) {
        Objects.requireNonNull(player, "player");
        if (!player.isOnline()) return;
        this.location = player.getLocation();
        this.location.setY(200);

        player.sendBlockChange(this.location, Material.OAK_SIGN.createBlockData());
        player.sendSignChange(this.location, this.text.toArray(new String[4]));

        PacketContainer openSign = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        BlockPosition position = new BlockPosition(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ());
        openSign.getBlockPositionModifier().write(0, position);
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSign);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        inputs.put(player, this);
    }
}
