package com.github.minersstudios.msutils.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;
import static com.github.minersstudios.msutils.utils.ChatUtils.sendJoinMessage;

public class PlayerInfo {
	private final @NotNull UUID uuid;
	private final @NotNull String nickname;

	private final @NotNull PlayerFile playerFile;

	private @Nullable Player onlinePlayer;
	private final @NotNull OfflinePlayer offlinePlayer;

	public PlayerInfo(@NotNull UUID uuid, @NotNull String nickname) {
		this.uuid = uuid;
		this.nickname = nickname;

		this.playerFile = PlayerFile.loadConfig(uuid, nickname);
		this.offlinePlayer = Bukkit.getOfflinePlayer(uuid);
	}

	public PlayerInfo(@NotNull UUID uuid) {
		this(uuid, Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid).getName()));
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
		int id = new PlayerID().getPlayerID(this.offlinePlayer.getUniqueId(), addPlayer, zeroIfNull);
		return this.playerFile.exists() ? id : zeroIfNull ? 0 : -1;
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
			getConfigCache().addMutedPlayer(this.onlinePlayer, date.getTime());
			ChatUtils.sendFine(sender,
					Component.text("Игрок : \"")
					.append(this.createGrayIDGreenName())
					.append(Component.text(" ("))
					.append(Component.text(this.nickname))
					.append(Component.text(")\" был замьючен :\n    - Причина : \""))
					.append(Component.text(reason))
					.append(Component.text("\"\n    - До : "))
					.append(Component.text(PlayerUtils.getDate(date, sender)))
			);
			ChatUtils.sendWarning(
					player,
					Component.text("Вы были замьючены : ")
					.append(Component.text("\n    - Причина : \""))
					.append(Component.text(reason))
					.append(Component.text("\"\n    - До : "))
					.append(Component.text(PlayerUtils.getDate(date, player)))
			);
			return;
		}
		getConfigCache().removeMutedPlayer(this.onlinePlayer);
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
			this.createPlayerDataFile();
		}

		this.playerFile.setBanned(value);
		this.playerFile.setBannedTo(date.getTime());
		this.playerFile.setBanReason(reason);
		this.playerFile.save();

		Player player = this.getOnlinePlayer();
		if (value) {
			Bukkit.getBanList(BanList.Type.NAME).addBan(this.nickname, reason, date, sender.getName());
			if (player != null && player.getAddress() != null) {
				PlayerUtils.kickPlayer(player, "Вы были забанены",
						reason
						+ "\"\n До : \n"
						+ PlayerUtils.getDate(date, player)
				);
			}
			ChatUtils.sendFine(sender,
					Component.text("Игрок : \"")
					.append(this.createGrayIDGreenName())
					.append(Component.text(" ("))
					.append(Component.text(this.nickname))
					.append(Component.text(")\" был забанен :\n    - Причина : \""))
					.append(Component.text(reason))
					.append(Component.text("\"\n    - До : "))
					.append(Component.text(PlayerUtils.getDate(date, sender)))
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

	public void createPlayerDataFile() {
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

	public @NotNull OfflinePlayer getOfflinePlayer() {
		return this.offlinePlayer;
	}

	public @Nullable Player getOnlinePlayer() {
		return this.onlinePlayer == null || !this.onlinePlayer.isOnline()
				? this.onlinePlayer = this.offlinePlayer.getPlayer() != null && this.offlinePlayer.isOnline()
						? this.offlinePlayer.getPlayer()
						: null
				: this.onlinePlayer;
	}

	public @NotNull UUID getUuid() {
		return this.uuid;
	}

	public @NotNull String getNickname() {
		return this.nickname;
	}
}
