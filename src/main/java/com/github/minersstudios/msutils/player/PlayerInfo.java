package com.github.minersstudios.msutils.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.DateUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.utils.IDUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.UUID;

import static com.github.minersstudios.mscore.utils.ChatUtils.extractMessage;
import static com.github.minersstudios.msutils.MSUtils.getConfigCache;
import static com.github.minersstudios.msutils.utils.MessageUtils.RolePlayActionType.ME;
import static com.github.minersstudios.msutils.utils.MessageUtils.RolePlayActionType.TODO;
import static com.github.minersstudios.msutils.utils.MessageUtils.sendJoinMessage;
import static com.github.minersstudios.msutils.utils.MessageUtils.sendRPEventMessage;

public class PlayerInfo {
	private final @NotNull UUID uuid;
	private final @NotNull String nickname;

	private @NotNull PlayerFile playerFile;

	private final @NotNull OfflinePlayer offlinePlayer;

	public PlayerInfo(@NotNull UUID uuid, @NotNull String nickname) {
		this.uuid = uuid;
		this.nickname = nickname;

		this.playerFile = PlayerFile.loadConfig(uuid, nickname);
		this.offlinePlayer = PlayerUtils.getOfflinePlayer(uuid, nickname);
	}

	public PlayerInfo(@NotNull Player player) {
		this.uuid = player.getUniqueId();
		this.nickname = player.getName();

		this.playerFile = PlayerFile.loadConfig(this.uuid, this.nickname);
		this.offlinePlayer = PlayerUtils.getOfflinePlayer(this.uuid, this.nickname);
	}

	public @NotNull Component createDefaultName() {
		return this.playerFile.getPlayerName().createDefaultName(this.getID());
	}

	public @NotNull Component createGoldenName() {
		return this.playerFile.getPlayerName().createGoldenName(this.getID());
	}

	public @NotNull Component createGrayIDGoldName() {
		return this.playerFile.getPlayerName().createGrayIDGoldName(this.getID());
	}

	public @NotNull Component createGrayIDGreenName() {
		return this.playerFile.getPlayerName().createGrayIDGreenName(this.getID());
	}

	public int getID(boolean addPlayer, boolean zeroIfNull) {
		return IDUtils.getID(this.offlinePlayer.getUniqueId(), addPlayer, zeroIfNull);
	}

	public int getID() {
		if (this == MSUtils.CONSOLE_PLAYER_INFO) return -1;
		return this.getID(false, true);
	}

	public boolean isMuted() {
		return this.playerFile.isMuted();
	}

	public @NotNull String getMuteReason() {
		return this.playerFile.getMuteReason();
	}

	public long getMutedTo() {
		return this.playerFile.getMutedTo();
	}

	public void setMuted(boolean value, @NotNull Date date, @NotNull String reason, CommandSender sender) {
		if (!this.playerFile.exists()) {
			ChatUtils.sendWarning(sender, Component.text("Данный игрок ещё ни разу не играл на сервере"));
			return;
		}

		this.playerFile.setMuted(value);
		this.playerFile.setMutedTo(date.getTime());
		this.playerFile.setMuteReason(reason);
		this.playerFile.save();

		Player player = this.getOnlinePlayer();
		if (value && player != null && player.getAddress() != null) {
			getConfigCache().addMutedPlayer(this.offlinePlayer, date.getTime());
			ChatUtils.sendFine(sender,
					Component.text("Игрок : \"")
					.append(this.createGrayIDGreenName())
					.append(Component.text(" ("))
					.append(Component.text(this.nickname))
					.append(Component.text(")\" был замьючен :\n    - Причина : \""))
					.append(Component.text(reason))
					.append(Component.text("\"\n    - До : "))
					.append(Component.text(DateUtils.getDate(date, sender)))
			);
			ChatUtils.sendWarning(
					player,
					Component.text("Вы были замьючены : ")
					.append(Component.text("\n    - Причина : \""))
					.append(Component.text(reason))
					.append(Component.text("\"\n    - До : "))
					.append(Component.text(DateUtils.getDate(date, player)))
			);
			return;
		}
		getConfigCache().removeMutedPlayer(this.offlinePlayer);
		ChatUtils.sendFine(sender,
				Component.text("Игрок : \"")
				.append(this.createGrayIDGreenName())
				.append(Component.text(" ("))
				.append(Component.text(this.playerFile.getPlayerName().getNickname()))
				.append(Component.text(")\" был размучен"))
		);
		if (player != null) {
			ChatUtils.sendWarning(player, Component.text("Вы были размучены"));
		}
	}

	public void setMuted(boolean value, CommandSender commandSender) {
		this.setMuted(value, new Date(0), "", commandSender);
	}

	public boolean isBanned() {
		return this.playerFile.isBanned() || this.offlinePlayer.isBanned();
	}

	public @NotNull String getBanReason() {
		return this.playerFile.getBanReason();
	}

	public long getBannedTo() {
		return this.playerFile.getBannedTo();
	}

	public void setBanned(boolean value, @NotNull Date date, @NotNull String reason, @NotNull CommandSender sender) {
		if (!this.playerFile.exists()) {
			this.createPlayerFile();
		}

		this.playerFile.setBanned(value);
		this.playerFile.setBannedTo(date.getTime());
		this.playerFile.setBanReason(reason);
		this.playerFile.save();

		if (value) {
			Bukkit.getBanList(BanList.Type.NAME).addBan(this.nickname, reason, date, sender.getName());
			this.kickPlayer("Вы были забанены",
					reason
					+ "\"\n До : \n"
					+ DateUtils.getDate(date, this.getOnlinePlayer())
			);
			ChatUtils.sendFine(sender,
					Component.text("Игрок : \"")
					.append(this.createGrayIDGreenName())
					.append(Component.text(" ("))
					.append(Component.text(this.nickname))
					.append(Component.text(")\" был забанен :\n    - Причина : \""))
					.append(Component.text(reason))
					.append(Component.text("\"\n    - До : "))
					.append(Component.text(DateUtils.getDate(date, sender)))
			);
		} else {
			Bukkit.getBanList(BanList.Type.NAME).pardon(this.nickname);
			ChatUtils.sendFine(sender,
					Component.text("Игрок : \"")
					.append(this.createGrayIDGreenName())
					.append(Component.text(" ("))
					.append(Component.text(this.nickname))
					.append(Component.text(")\" был разбанен"))
			);
		}
	}

	public void setBanned(boolean value, @NotNull CommandSender commandSender) {
		this.setBanned(value, new Date(0), "", commandSender);
	}

	public void setBanned(boolean value) {
		this.setBanned(value, new Date(0), "", Bukkit.getConsoleSender());
	}

	public void teleportToDarkWorld() {
		Player player = this.getOnlinePlayer();
		if (player == null) return;
		if (player.isDead()) {
			player.spigot().respawn();
		}
		player.teleportAsync(new Location(MSUtils.getWorldDark(), 0.0d, 0.0d, 0.0d), PlayerTeleportEvent.TeleportCause.PLUGIN);
	}

	public void teleportToLastLeaveLocation() {
		Player player = this.getOnlinePlayer();
		if (player == null) return;
		player.setGameMode(this.playerFile.getGameMode());
		player.setHealth(this.playerFile.getHealth());
		player.setRemainingAir(this.playerFile.getAir());
		player.setFlying(false);
		player.setAllowFlight(false);

		Location leaveLoc = this.playerFile.getLastLeaveLocation();
		player.teleportAsync(
				leaveLoc == null ? MSUtils.getOverworld().getSpawnLocation() : leaveLoc,
				PlayerTeleportEvent.TeleportCause.PLUGIN
		);

		Bukkit.getScheduler().runTaskAsynchronously(MSUtils.getInstance(),
				() -> sendJoinMessage(this)
		);
	}

	public void setLastLeaveLocation() {
		Player player = this.getOnlinePlayer();
		if (
				player == null
				|| player.getWorld() == MSUtils.getWorldDark()
		) return;
		Location leaveLocation = player.getLocation();
		this.playerFile.setLastLeaveLocation(
				player.isDead()
				? player.getBedSpawnLocation() != null
						? player.getBedSpawnLocation()
						: MSUtils.getOverworld().getSpawnLocation()
				: leaveLocation);
		this.playerFile.save();
	}

	public void setLastDeathLocation() {
		Player player = this.getOnlinePlayer();
		if (
				player == null
				|| player.getWorld() == MSUtils.getWorldDark()
		) return;
		this.playerFile.setLastDeathLocation(player.getLocation());
		this.playerFile.save();
	}

	public void createPlayerFile() {
		if (this.playerFile.exists()) return;
		this.playerFile.getYamlConfiguration().set("name.nickname", this.nickname);
		if (this.getOnlinePlayer() != null) {
			this.playerFile.addIp(
					this.getOnlinePlayer().getAddress() == null
					? null
					: this.getOnlinePlayer().getAddress().getAddress().getHostAddress()
			);
			this.playerFile.setFirstJoin(System.currentTimeMillis());
		}
		this.playerFile.save();
		ChatUtils.sendFine(null, Component.text(
				"Создан файл с данными игрока : \"" + this.nickname + "\" с названием : \"" + this.offlinePlayer.getUniqueId() + ".yml\""
		));
	}

	public @NotNull PlayerFile getPlayerFile() {
		return this.playerFile;
	}

	public void update() {
		this.playerFile = PlayerFile.loadConfig(this.uuid, this.nickname);
	}

	public @NotNull OfflinePlayer getOfflinePlayer() {
		return this.offlinePlayer;
	}

	public @Nullable Player getOnlinePlayer() {
		return this.offlinePlayer.getPlayer();
	}

	/**
	 * @return True if the player isn't in dark_world and hasn't vanished
	 */
	public boolean isOnline() {
		return this.isOnline(false);
	}

	/**
	 * @param ignoreWorld ignore world_dark check
	 * @return True if the player isn't in dark_world and hasn't vanished
	 */
	public boolean isOnline(boolean ignoreWorld) {
		Player player = this.offlinePlayer.getPlayer();
		return player != null
				&& (ignoreWorld || player.getWorld() != MSUtils.getWorldDark())
				&& !this.isVanished();
	}

	public boolean isVanished() {
		Player player = this.getOnlinePlayer();
		return player != null && player.getMetadata("vanished").stream().anyMatch(MetadataValue::asBoolean);
	}

	public boolean kickPlayer(@NotNull String title, @NotNull String reason) {
		Player player = this.getOnlinePlayer();
		if (
				player == null
				|| !player.isOnline()
				|| player.getPlayer() == null
		) return false;
		this.setLastLeaveLocation();
		this.playerFile.setGameMode(player.getGameMode());
		this.playerFile.setHealth(player.getHealth());
		this.playerFile.setAir(player.getRemainingAir());
		this.playerFile.save();
		player.kick(
				Component.empty()
				.append(Component.text(title).color(NamedTextColor.RED).decorate(TextDecoration.BOLD))
				.append(Component.text("\n\n<---====+====--->\n", NamedTextColor.DARK_GRAY))
				.append(Component.text("\nПричина :\n\"")
				.append(Component.text(reason)
				.append(Component.text("\"")))
				.color(NamedTextColor.GRAY))
				.append(Component.text("\n\n<---====+====--->\n", NamedTextColor.DARK_GRAY))
		);
		return true;
	}

	public void setSitting(@Nullable Location sitLocation, String @Nullable ... args) {
		Player player = this.getOnlinePlayer();
		if (
				player == null
				|| (player.getVehicle() != null
				&& player.getVehicle().getType() != EntityType.ARMOR_STAND)
		) return;
		if (
				!getConfigCache().seats.containsKey(player)
				&& sitLocation != null
		) {
			player.getWorld().spawn(sitLocation.clone().subtract(0.0d, 0.95d, 0.0d), ArmorStand.class, (armorStand) -> {
				armorStand.setGravity(false);
				armorStand.setVisible(false);
				armorStand.setCollidable(false);
				armorStand.setSmall(true);
				armorStand.addPassenger(player);
				armorStand.addScoreboardTag("customDecor");
				getConfigCache().seats.put(player, armorStand);
			});
			if (args == null) {
				sendRPEventMessage(player, Component.text(this.playerFile.getPronouns().getSitMessage()), ME);
			} else {
				sendRPEventMessage(player, Component.text(extractMessage(args, 0)), Component.text("приседая"), TODO);
			}
		} else if (sitLocation == null && getConfigCache().seats.containsKey(player)) {
			ArmorStand armorStand = getConfigCache().seats.remove(player);
			Location playerLoc = player.getLocation();
			Location getUpLocation = armorStand.getLocation().add(0.0d, 1.7d, 0.0d);

			getUpLocation.setYaw(playerLoc.getYaw());
			getUpLocation.setPitch(playerLoc.getPitch());
			armorStand.remove();
			player.teleport(getUpLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
			sendRPEventMessage(player, Component.text(this.playerFile.getPronouns().getUnSitMessage()), ME);
		}
	}

	public void unsetSitting(String @Nullable ... args) {
		this.setSitting(null, args);
	}

	public boolean setWhiteListed(boolean value) {
		if (value) {
			if (Bukkit.getWhitelistedPlayers().contains(this.offlinePlayer)) return false;
			this.offlinePlayer.setWhitelisted(true);
		} else {
			if (!Bukkit.getWhitelistedPlayers().contains(this.offlinePlayer)) return false;
			this.offlinePlayer.setWhitelisted(false);
			this.kickPlayer("Вы были кикнуты", "Вас удалили из белого списка");
		}
		return true;
	}

	public @Nullable Player loadPlayerData() {
		return PlayerUtils.loadPlayer(this.offlinePlayer);
	}

	public @NotNull UUID getUuid() {
		return this.uuid;
	}

	public @NotNull String getNickname() {
		return this.nickname;
	}
}
