package github.minersStudios.msUtils.classes;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import javax.annotation.Nullable;

public record SitPlayer(Player player) {

	public void setSitting(@Nullable Location sitLocation) {
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (!this.isSitting() && sitLocation != null) {
			player.getWorld().spawn(sitLocation.clone().subtract(0.0d, 1.7d, 0.0d), ArmorStand.class, (armorStand) ->{
				armorStand.setGravity(false);
				armorStand.setVisible(false);
				armorStand.addPassenger(this.player);
				armorStand.addScoreboardTag("customDecor");
				Main.plugin.getSeats().put(this.player.getUniqueId(), armorStand);
				ChatUtils.sendRPEventMessage(player, 25,
						"* "
						+ playerInfo.getGrayIDGoldName() + " "
						+ ChatColor.GOLD + playerInfo.getPronouns().getSitMessage()
						+ "*"
				);
			});
		} else if (sitLocation == null && this.isSitting()) {
			ArmorStand armorStand = Main.plugin.getSeats().get(this.player.getUniqueId());
			Main.plugin.getSeats().remove(this.player.getUniqueId());
			this.player.eject();
			this.player.teleport(armorStand.getLocation().add(0.0d, 1.7d, 0.0d));
			armorStand.remove();
			ChatUtils.sendRPEventMessage(player, 25,
					"* "
					+ playerInfo.getGrayIDGoldName() + " "
					+ ChatColor.GOLD + playerInfo.getPronouns().getUnSitMessage()
					+ "*"
			);
		}
	}

	public boolean isSitting() {
		return Main.plugin.getSeats().containsKey(this.player.getUniqueId());
	}
}
