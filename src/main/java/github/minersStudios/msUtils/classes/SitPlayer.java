package github.minersStudios.msUtils.classes;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public record SitPlayer(Player player) {

    public void setSitting(boolean arg) {
        PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
        if (arg && !this.isSitting()) {
            Location location = this.player.getLocation();
            ArmorStand armorStand = location.getBlock().getWorld().spawn(location.clone().subtract(0.0d, 1.7d, 0.0d), ArmorStand.class);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.addPassenger(this.player);
            Main.plugin.getSeats().put(this.player.getUniqueId(), armorStand);
            ChatUtils.sendRPEventMessage(player, 25, ChatColor.GOLD + "*"
                    + ChatColor.GRAY + " [" + playerInfo.getID() + "] "
                    + ChatColor.GOLD + playerInfo.getFirstname() + " " + playerInfo.getLastname() + " "
                    + (playerInfo.getPronouns() != null ? playerInfo.getPronouns().getSitMessage() : Pronouns.HE.getSitMessage())
                    + "*");
        } else if (!arg && this.isSitting()) {
            ArmorStand armorStand = Main.plugin.getSeats().get(this.player.getUniqueId());
            Main.plugin.getSeats().remove(this.player.getUniqueId());
            this.player.eject();
            this.player.teleport(armorStand.getLocation().add(0.0d, 1.7d, 0.0d));
            armorStand.remove();
            ChatUtils.sendRPEventMessage(player, 25, ChatColor.GOLD + "*"
                    + ChatColor.GRAY + " [" + playerInfo.getID() + "] "
                    + ChatColor.GOLD + playerInfo.getFirstname() + " " + playerInfo.getLastname() + " "
                    + (playerInfo.getPronouns() != null ? playerInfo.getPronouns().getUnSitMessage() : Pronouns.HE.getUnSitMessage())
                    + "*");
        }
    }

    public boolean isSitting() {
        return Main.plugin.getSeats().containsKey(this.player.getUniqueId());
    }
}
