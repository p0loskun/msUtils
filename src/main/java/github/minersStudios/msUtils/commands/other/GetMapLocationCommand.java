package github.minersStudios.msUtils.commands.other;

import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
            return ChatUtils.sendError(sender, "Только игрок может использовать эту команду!");
        } else {
            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
            if (!itemInMainHand.getType().equals(Material.FILLED_MAP)) return ChatUtils.sendWarning(player, "Возьмите в правую руку карту!");
            MapMeta mapMeta = (MapMeta) itemInMainHand.getItemMeta();
            assert mapMeta != null;
            MapView mapView = mapMeta.getMapView();
            if (mapView != null && mapView.getWorld() != null) {
                ChatUtils.sendWarning(player,
                        "Мир карты : " +
                        "\n  " + ChatColor.WHITE + mapView.getWorld().getName() +
                        "\n ꀓ " + ChatColor.GOLD + "Координаты точки центра карты : " +
                        ChatColor.GREEN + "\n    - X : " + ChatColor.WHITE + mapView.getCenterX() +
                        ChatColor.GREEN + "\n    - Y : " + ChatColor.WHITE + "~" +
                        ChatColor.GREEN + "\n    - Z : " + ChatColor.WHITE + mapView.getCenterZ()
                );
            }
        }
        return true;
    }
}
