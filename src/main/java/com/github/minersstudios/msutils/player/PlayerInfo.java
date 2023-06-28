package com.github.minersstudios.msutils.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.DateUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import com.mojang.authlib.GameProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.server.players.UserWhiteList;
import net.minecraft.server.players.UserWhiteListEntry;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;
import static com.github.minersstudios.msutils.utils.MessageUtils.RolePlayActionType.ME;
import static com.github.minersstudios.msutils.utils.MessageUtils.RolePlayActionType.TODO;
import static com.github.minersstudios.msutils.utils.MessageUtils.*;
import static net.kyori.adventure.text.Component.text;

@SuppressWarnings("unused")
public class PlayerInfo {
    private final @NotNull UUID uuid;
    private final @NotNull String nickname;
    private @NotNull PlayerFile playerFile;
    private final @NotNull OfflinePlayer offlinePlayer;
    private Component defaultName;
    private Component goldenName;
    private Component grayIDGoldName;
    private Component grayIDGreenName;

    public PlayerInfo(
            @NotNull UUID uuid,
            @NotNull String nickname
    ) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.playerFile = PlayerFile.loadConfig(uuid, nickname);
        this.offlinePlayer = PlayerUtils.getOfflinePlayer(uuid, nickname);

        this.initNames();
    }

    public PlayerInfo(@NotNull Player player) {
        this.uuid = player.getUniqueId();
        this.nickname = player.getName();
        this.playerFile = PlayerFile.loadConfig(this.uuid, this.nickname);
        this.offlinePlayer = PlayerUtils.getOfflinePlayer(this.uuid, this.nickname);

        this.initNames();
    }

    public @NotNull Component getDefaultName() {
        return this.defaultName;
    }

    public @NotNull Component getGoldenName() {
        return this.goldenName;
    }

    public @NotNull Component getGrayIDGoldName() {
        return this.grayIDGoldName;
    }

    public @NotNull Component getGrayIDGreenName() {
        return this.grayIDGreenName;
    }

    public void initNames() {
        int id = this.getID();
        PlayerName playerName = this.playerFile.getPlayerName();
        this.defaultName = playerName.createDefaultName(id);
        this.goldenName = playerName.createGoldenName(id);
        this.grayIDGoldName = playerName.createGrayIDGoldName(id);
        this.grayIDGreenName = playerName.createGrayIDGreenName(id);
    }

    public int getID(
            boolean addPlayer,
            boolean zeroIfNull
    ) {
        return this == MSUtils.getConsolePlayerInfo()
                ? -1
                : getConfigCache().idMap.get(this.offlinePlayer.getUniqueId(), addPlayer, zeroIfNull);
    }

    public int getID() {
        return this.getID(false, true);
    }

    public boolean isMuted() {
        return getConfigCache().muteMap.isMuted(this.offlinePlayer);
    }

    public @Nullable MuteMap.Params getMuteParams() {
        return getConfigCache().muteMap.getParams(this.offlinePlayer);
    }

    public @NotNull String getMuteReason() throws IllegalStateException {
        MuteMap.Params params = this.getMuteParams();
        if (params == null) {
            throw new IllegalStateException("Player is not muted");
        }
        return params.getReason();
    }

    public @NotNull String getMutedBy() throws IllegalStateException {
        MuteMap.Params params = this.getMuteParams();
        if (params == null) {
            throw new IllegalStateException("Player is not muted");
        }
        return params.getSource();
    }

    public @NotNull Component getMutedFrom(@NotNull CommandSender sender) throws IllegalStateException {
        return text(DateUtils.getSenderDate(this.getMutedFrom(), sender));
    }

    public @NotNull Component getMutedFrom(@NotNull InetAddress address) throws IllegalStateException {
        return text(DateUtils.getDate(this.getMutedFrom(), address));
    }

    public @NotNull Instant getMutedFrom() throws IllegalStateException {
        MuteMap.Params params = this.getMuteParams();
        if (params == null) {
            throw new IllegalStateException("Player is not muted");
        }
        return params.getCreated();
    }

    public @NotNull Component getMutedTo(@NotNull CommandSender sender) throws IllegalStateException {
        return text(DateUtils.getSenderDate(this.getMutedTo(), sender));
    }

    public @NotNull Component getMutedTo(@NotNull InetAddress address) throws IllegalStateException {
        return text(DateUtils.getDate(this.getMutedTo(), address));
    }

    public @NotNull Instant getMutedTo() throws IllegalStateException {
        MuteMap.Params params = this.getMuteParams();
        if (params == null) {
            throw new IllegalStateException("Player is not muted");
        }
        return params.getExpiration();
    }

    public void setMuted(
            boolean value,
            @NotNull Instant date,
            @NotNull String reason,
            CommandSender sender
    ) {
        Player player = this.getOnlinePlayer();
        MuteMap muteMap = getConfigCache().muteMap;

        if (value) {
            if (this.isMuted()) {
                ChatUtils.sendWarning(
                        sender,
                        Component.translatable(
                                "ms.command.mute.already.sender",
                                this.getGrayIDGoldName(),
                                text(this.nickname)
                        )
                );
                return;
            }

            muteMap.put(this.offlinePlayer, date, reason, sender.getName());
            ChatUtils.sendFine(
                    sender,
                    Component.translatable(
                            "ms.command.mute.message.sender",
                            this.getGrayIDGreenName(),
                            text(this.nickname),
                            text(reason),
                            text(DateUtils.getSenderDate(date, sender))
                    )
            );

            if (player != null) {
                ChatUtils.sendWarning(
                        player,
                        Component.translatable(
                                "ms.command.mute.message.receiver",
                                text(reason),
                                text(DateUtils.getSenderDate(date, sender))
                        )
                );
            }
        } else {
            if (!this.isMuted()) {
                ChatUtils.sendWarning(
                        sender,
                        Component.translatable(
                                "ms.command.unmute.not_muted",
                                this.getGrayIDGoldName(),
                                text(this.nickname)
                        )
                );
                return;
            }

            muteMap.remove(this.offlinePlayer);
            ChatUtils.sendFine(
                    sender,
                    Component.translatable(
                            "ms.command.unmute.sender.message",
                            this.getGrayIDGreenName(),
                            text(this.nickname)
                    )
            );

            if (player != null) {
                ChatUtils.sendWarning(player, Component.translatable("ms.command.unmute.receiver.message"));
            }
        }
    }

    public void setMuted(
            boolean value,
            CommandSender commandSender
    ) {
        this.setMuted(value, Instant.EPOCH, "", commandSender);
    }

    public @Nullable BanEntry getBanEntry() {
        return Bukkit.getBanList(BanList.Type.NAME).getBanEntry(this.nickname);
    }

    public boolean isBanned() {
        return this.getBanEntry() != null;
    }

    public @NotNull Component getBanReason() throws IllegalStateException {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        String reason = banEntry.getReason();
        return reason == null ? text("неизвестно") : text(reason);
    }

    public void setBanReason(@NotNull String reason) {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        banEntry.setReason(reason);
        banEntry.save();
    }

    public @NotNull String getBannedBy() throws IllegalStateException {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        return banEntry.getSource();
    }

    public void setBannedBy(@NotNull String source) {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        banEntry.setSource(source);
        banEntry.save();
    }

    public @NotNull Component getBannedFrom(@NotNull CommandSender sender) throws IllegalStateException {
        return text(DateUtils.getSenderDate(this.getBannedFrom(), sender));
    }

    public @NotNull Component getBannedFrom(@NotNull InetAddress address) throws IllegalStateException {
        return text(DateUtils.getDate(this.getBannedFrom(), address));
    }

    public @NotNull Instant getBannedFrom() throws IllegalStateException {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        return banEntry.getCreated().toInstant();
    }

    public @NotNull Component getBannedTo(@NotNull CommandSender sender) throws IllegalStateException {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        Date expiration = banEntry.getExpiration();
        return expiration == null
                ? Component.translatable("ms.command.ban.time.forever")
                : text(DateUtils.getSenderDate(expiration.toInstant(), sender));
    }

    public @NotNull Component getBannedTo(@NotNull InetAddress address) throws IllegalStateException {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        Date expiration = banEntry.getExpiration();
        return expiration == null
                ? Component.translatable("ms.command.ban.time.forever")
                : text(DateUtils.getDate(expiration.toInstant(), address));
    }

    public @Nullable Date getBannedTo() throws IllegalStateException {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        return banEntry.getExpiration();
    }

    public void setBannedTo(@NotNull Date expiration) {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        banEntry.setExpiration(expiration);
        banEntry.save();
    }

    public void setBanned(
            boolean value,
            @NotNull Instant date,
            @NotNull String reason,
            @NotNull CommandSender sender
    ) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);

        if (value) {
            if (this.isBanned()) {
                ChatUtils.sendWarning(
                        sender,
                        Component.translatable(
                                "ms.command.ban.already.sender",
                                this.getGrayIDGoldName(),
                                text(this.nickname)
                        )
                );
                return;
            }

            banList.addBan(this.nickname, reason, Date.from(date), sender.getName());
            this.kickPlayer(
                    Component.translatable("ms.command.ban.message.receiver.title"),
                    Component.translatable(
                            "ms.command.ban.message.receiver.subtitle",
                            text(reason),
                            text(DateUtils.getSenderDate(date, this.getOnlinePlayer()))
                    )
            );
            ChatUtils.sendFine(
                    sender,
                    Component.translatable(
                            "ms.command.ban.message.sender",
                            this.getGrayIDGreenName(),
                            text(this.nickname),
                            text(reason),
                            text(DateUtils.getSenderDate(date, sender))
                    )
            );
        } else {
            if (!this.isBanned()) {
                ChatUtils.sendWarning(
                        sender,
                        Component.translatable(
                                "ms.command.unban.not_banned",
                                this.getGrayIDGoldName(),
                                text(this.nickname)
                        )
                );
                return;
            }

            banList.pardon(this.nickname);
            ChatUtils.sendFine(
                    sender,
                    Component.translatable(
                            "ms.command.unban.message.sender",
                            this.getGrayIDGreenName(),
                            text(this.nickname)
                    )
            );
        }
    }

    public void setBanned(
            boolean value,
            @NotNull CommandSender commandSender
    ) {
        this.setBanned(value, Instant.EPOCH, "", commandSender);
    }

    public void setBanned(boolean value) {
        this.setBanned(value, Instant.EPOCH, "", Bukkit.getConsoleSender());
    }

    public void initJoin() {
        Player player = this.getOnlinePlayer();
        if (player == null) return;

        player.setGameMode(this.playerFile.getGameMode());
        player.setHealth(this.playerFile.getHealth());
        player.setRemainingAir(this.playerFile.getAir());

        this.teleportToLastLeaveLocation();

        Bukkit.getScheduler().runTaskAsynchronously(
                MSUtils.getInstance(),
                () -> sendJoinMessage(this)
        );
    }

    public void initQuit() {
        Player player = this.getOnlinePlayer();
        if (player == null) return;

        this.unsetSitting();

        Entity vehicle = player.getVehicle();
        if (vehicle != null) {
            vehicle.eject();
        }

        MSUtils.getConfigCache().playerAnomalyActionMap.remove(player);
        this.savePlayerDataParams();

        if (!this.isInWorldDark()) {
            sendQuitMessage(this, player);
        }
    }

    public void teleportToLastLeaveLocation() {
        Player player = this.getOnlinePlayer();
        if (player == null) return;

        Bukkit.getScheduler().runTask(MSUtils.getInstance(), () -> {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                player.setSpectatorTarget(null);
            }
        });

        Location location = this.playerFile.getLastLeaveLocation();
        player.teleportAsync(
                location == null ? MSUtils.getOverworld().getSpawnLocation() : location,
                PlayerTeleportEvent.TeleportCause.PLUGIN
        );
    }

    public void setLastLeaveLocation(@Nullable Location location) {
        Player player = this.getOnlinePlayer();

        if (
                player == null
                || this.isInWorldDark()
        ) return;

        this.playerFile.setLastLeaveLocation(
                player.isDead()
                        ? player.getBedSpawnLocation() != null
                        ? player.getBedSpawnLocation()
                        : MSUtils.getOverworld().getSpawnLocation()
                        : location
        );
        this.playerFile.save();
    }

    public void teleportToLastDeathLocation() {
        Player player = this.getOnlinePlayer();

        if (player == null) return;

        Bukkit.getScheduler().runTask(MSUtils.getInstance(), () -> {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                player.setSpectatorTarget(null);
            }
        });

        Location location = this.playerFile.getLastDeathLocation();
        player.teleportAsync(
                location == null ? MSUtils.getOverworld().getSpawnLocation() : location,
                PlayerTeleportEvent.TeleportCause.PLUGIN
        );
    }

    public void setLastDeathLocation(@Nullable Location location) {
        Player player = this.getOnlinePlayer();

        if (
                player == null
                || this.isInWorldDark()
        ) return;

        this.playerFile.setLastDeathLocation(location);
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
            this.playerFile.setFirstJoin(Instant.now());
        }

        this.playerFile.save();
        ChatUtils.sendFine(
                Component.translatable(
                        "ms.info.player_file_created",
                        text(this.nickname),
                        text(this.offlinePlayer.getUniqueId().toString())
                )
        );
    }

    public @NotNull PlayerFile getPlayerFile() {
        return this.playerFile;
    }

    public void update() {
        this.playerFile = PlayerFile.loadConfig(this.uuid, this.nickname);
        this.initNames();
    }

    public @NotNull OfflinePlayer getOfflinePlayer() {
        return this.offlinePlayer;
    }

    public @Nullable Player getOnlinePlayer() {
        return Bukkit.getPlayer(this.uuid);
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
                && (ignoreWorld || !this.isInWorldDark())
                && !this.isVanished();
    }

    public boolean isVanished() {
        Player player = this.getOnlinePlayer();
        return player != null && player.getMetadata("vanished").stream().anyMatch(MetadataValue::asBoolean);
    }

    public boolean kickPlayer(
            @NotNull Component title,
            @NotNull Component reason
    ) {
        Player player = this.getOnlinePlayer();

        if (
                player == null
                || !player.isOnline()
                || player.getPlayer() == null
        ) return false;

        this.initQuit();
        player.kick(
                Component.translatable(
                        "ms.format.leave.message",
                        title.color(NamedTextColor.RED).decorate(TextDecoration.BOLD),
                        reason.color(NamedTextColor.GRAY)
                ).color(NamedTextColor.DARK_GRAY)
        );
        return true;
    }

    public boolean isSitting() {
        Player player = this.getOnlinePlayer();
        return player != null && getConfigCache().seats.containsKey(player);
    }

    public void setSitting(
            @NotNull Location sitLocation,
            @Nullable Component message
    ) {
        Player player = this.getOnlinePlayer();

        if (
                player == null
                || (player.getVehicle() != null
                && player.getVehicle().getType() != EntityType.ARMOR_STAND)
                || this.isSitting()
        ) return;

        player.getWorld().spawn(sitLocation.clone().subtract(0.0d, 0.2d, 0.0d), ArmorStand.class, (armorStand) -> {
            armorStand.setMarker(true);
            armorStand.setCanTick(false);
            armorStand.setBasePlate(false);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCollidable(false);
            armorStand.setSmall(true);
            armorStand.addPassenger(player);
            armorStand.addScoreboardTag("customDecor");
            getConfigCache().seats.put(player, armorStand);
        });

        if (message == null) {
            sendRPEventMessage(player, this.playerFile.getPronouns().getSitMessage(), ME);
        } else {
            sendRPEventMessage(player, message, text("приседая"), TODO);
        }
    }

    public void setSitting(@NotNull Location sitLocation) {
        this.setSitting(sitLocation, null);
    }

    public void unsetSitting(@Nullable Component message) {
        Player player = this.getOnlinePlayer();

        if (
                player == null
                || (player.getVehicle() != null
                && player.getVehicle().getType() != EntityType.ARMOR_STAND)
                || !isSitting()
        ) return;

        ArmorStand armorStand = getConfigCache().seats.remove(player);
        Location playerLoc = player.getLocation();
        Location getUpLocation = armorStand.getLocation().add(0.0d, 0.5d, 0.0d);

        getUpLocation.setYaw(playerLoc.getYaw());
        getUpLocation.setPitch(playerLoc.getPitch());
        armorStand.remove();
        player.teleport(getUpLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);

        if (message == null) {
            sendRPEventMessage(player, this.playerFile.getPronouns().getUnSitMessage(), ME);
        } else {
            sendRPEventMessage(player, message, text("вставая"), TODO);
        }
    }

    public void unsetSitting() {
        this.unsetSitting(null);
    }

    public boolean setWhiteListed(boolean value) {
        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        UserWhiteList userWhiteList = craftServer.getServer().getPlayerList().getWhiteList();
        GameProfile gameProfile = new GameProfile(this.uuid, this.nickname);
        boolean contains = Bukkit.getWhitelistedPlayers().contains(this.offlinePlayer);

        if (value) {
            if (contains) return false;
            userWhiteList.add(new UserWhiteListEntry(gameProfile));
        } else {
            if (!contains) return false;
            userWhiteList.remove(gameProfile);
            this.kickPlayer(
                    Component.translatable("ms.command.white_list.remove.receiver.message.title"),
                    Component.translatable("ms.command.white_list.remove.receiver.message.subtitle")
            );
        }
        return true;
    }

    public boolean isInWorldDark() {
        Player player = this.getOnlinePlayer();
        return player != null && player.getWorld().equals(MSUtils.getWorldDark());
    }

    public void savePlayerDataParams() {
        Player player = this.getOnlinePlayer();

        if (
                player == null
                || this.isInWorldDark()
        ) return;

        double health = player.getHealth();
        int air = player.getRemainingAir();

        this.setLastLeaveLocation(player.getLocation());
        this.playerFile.setGameMode(player.getGameMode());
        this.playerFile.setHealth(health == 0.0d ? 20.0d : health);
        this.playerFile.setAir(air == 0 && player.isDead() ? 300 : air);
        this.playerFile.save();
    }

    public void hideNameTag() {
        Player player = this.getOnlinePlayer();
        if (player == null) return;
        MSPlayerUtils.hideNameTag(player);
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
