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
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.entity.ArmorStand;
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

import static com.github.minersstudios.mscore.utils.ChatUtils.extractMessage;
import static com.github.minersstudios.msutils.MSUtils.getConfigCache;
import static com.github.minersstudios.msutils.utils.MessageUtils.RolePlayActionType.ME;
import static com.github.minersstudios.msutils.utils.MessageUtils.RolePlayActionType.TODO;
import static com.github.minersstudios.msutils.utils.MessageUtils.sendJoinMessage;
import static com.github.minersstudios.msutils.utils.MessageUtils.sendRPEventMessage;
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

    public @NotNull String getMutedFrom(@NotNull CommandSender sender) throws IllegalStateException {
        return DateUtils.getSenderDate(Date.from(this.getMutedFrom()), sender);
    }

    public @NotNull String getMutedFrom(@NotNull InetAddress address) throws IllegalStateException {
        return DateUtils.getDate(Date.from(this.getMutedFrom()), address);
    }

    public @NotNull Instant getMutedFrom() throws IllegalStateException {
        MuteMap.Params params = this.getMuteParams();
        if (params == null) {
            throw new IllegalStateException("Player is not muted");
        }
        return params.getCreated();
    }

    public @NotNull String getMutedTo(@NotNull CommandSender sender) throws IllegalStateException {
        return DateUtils.getSenderDate(Date.from(this.getMutedTo()), sender);
    }

    public @NotNull String getMutedTo(@NotNull InetAddress address) throws IllegalStateException {
        return DateUtils.getDate(Date.from(this.getMutedTo()), address);
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
            @NotNull Date date,
            @NotNull String reason,
            CommandSender sender
    ) {
        Player player = this.getOnlinePlayer();
        MuteMap muteMap = getConfigCache().muteMap;

        if (value) {
            if (this.isMuted()) {
                ChatUtils.sendWarning(sender,
                        text("Игрок : \"")
                        .append(this.getGrayIDGoldName())
                        .append(text(" ("))
                        .append(text(this.nickname))
                        .append(text(")\" уже замьючен"))
                );
                return;
            }

            muteMap.put(this.offlinePlayer, date.toInstant(), reason, sender.getName());
            ChatUtils.sendFine(sender,
                    text("Игрок : \"")
                    .append(this.getGrayIDGreenName())
                    .append(text(" ("))
                    .append(text(this.nickname))
                    .append(text(")\" был замьючен :\n    - Причина : \""))
                    .append(text(reason))
                    .append(text("\"\n    - До : "))
                    .append(text(DateUtils.getSenderDate(date, sender)))
            );

            if (player != null) {
                ChatUtils.sendWarning(
                        player,
                        text("Вы были замьючены : ")
                        .append(text("\n    - Причина : \""))
                        .append(text(reason))
                        .append(text("\"\n    - До : "))
                        .append(text(DateUtils.getSenderDate(date, player)))
                );
            }
        } else {
            if (!this.isMuted()) {
                ChatUtils.sendWarning(sender,
                        text("Игрок : \"")
                        .append(this.getGrayIDGoldName())
                        .append(text(" ("))
                        .append(text(this.nickname))
                        .append(text(")\" не замьючен"))
                );
                return;
            }

            muteMap.remove(this.offlinePlayer);
            ChatUtils.sendFine(sender,
                    text("Игрок : \"")
                    .append(this.getGrayIDGreenName())
                    .append(text(" ("))
                    .append(text(this.nickname))
                    .append(text(")\" был размучен"))
            );

            if (player != null) {
                ChatUtils.sendWarning(player, "Вы были размучены");
            }
        }
    }

    public void setMuted(
            boolean value,
            CommandSender commandSender
    ) {
        this.setMuted(value, new Date(0), "", commandSender);
    }

    public @Nullable BanEntry getBanEntry() {
        return Bukkit.getBanList(BanList.Type.NAME).getBanEntry(this.nickname);
    }

    public boolean isBanned() {
        return this.getBanEntry() != null;
    }

    public @NotNull String getBanReason() throws IllegalStateException {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        String reason = banEntry.getReason();
        return reason == null ? "неизвестно" : reason;
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

    public @NotNull String getBannedFrom(@NotNull CommandSender sender) throws IllegalStateException {
        return DateUtils.getSenderDate(this.getBannedFrom(), sender);
    }

    public @NotNull String getBannedFrom(@NotNull InetAddress address) throws IllegalStateException {
        return DateUtils.getDate(this.getBannedFrom(), address);
    }

    public @NotNull Date getBannedFrom() throws IllegalStateException {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        return banEntry.getCreated();
    }

    public @NotNull String getBannedTo(@NotNull CommandSender sender) throws IllegalStateException {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        Date expiration = banEntry.getExpiration();
        return expiration == null ? "навсегда" : DateUtils.getSenderDate(expiration, sender);
    }

    public @NotNull String getBannedTo(@NotNull InetAddress address) throws IllegalStateException {
        BanEntry banEntry = this.getBanEntry();
        if (banEntry == null) {
            throw new IllegalStateException("Player is not banned");
        }
        Date expiration = banEntry.getExpiration();
        return expiration == null ? "навсегда" : DateUtils.getDate(expiration, address);
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
            @NotNull Date date,
            @NotNull String reason,
            @NotNull CommandSender sender
    ) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);

        if (value) {
            if (this.isBanned()) {
                ChatUtils.sendWarning(sender,
                        text("Игрок : \"")
                        .append(this.getGrayIDGoldName())
                        .append(text(" ("))
                        .append(text(this.nickname))
                        .append(text(")\" уже забанен"))
                );
                return;
            }

            banList.addBan(this.nickname, reason, date, sender.getName());
            this.kickPlayer(
                    "Вы были забанены",
                    reason
                    + "\"\n До : \n"
                    + DateUtils.getSenderDate(date, this.getOnlinePlayer())
            );
            ChatUtils.sendFine(sender,
                    text("Игрок : \"")
                    .append(this.getGrayIDGreenName())
                    .append(text(" ("))
                    .append(text(this.nickname))
                    .append(text(")\" был забанен :\n    - Причина : \""))
                    .append(text(reason))
                    .append(text("\"\n    - До : "))
                    .append(text(DateUtils.getSenderDate(date, sender)))
            );
        } else {
            if (!this.isBanned()) {
                ChatUtils.sendWarning(sender,
                        text("Игрок : \"")
                        .append(this.getGrayIDGoldName())
                        .append(text(" ("))
                        .append(text(this.nickname))
                        .append(text(")\" не забанен"))
                );
                return;
            }

            banList.pardon(this.nickname);
            ChatUtils.sendFine(sender,
                    text("Игрок : \"")
                    .append(this.getGrayIDGreenName())
                    .append(text(" ("))
                    .append(text(this.nickname))
                    .append(text(")\" был разбанен"))
            );
        }
    }

    public void setBanned(
            boolean value,
            @NotNull CommandSender commandSender
    ) {
        this.setBanned(value, new Date(0), "", commandSender);
    }

    public void setBanned(boolean value) {
        this.setBanned(value, new Date(0), "", Bukkit.getConsoleSender());
    }

    public void initJoin() {
        Player player = this.getOnlinePlayer();
        if (player == null) return;

        player.setGameMode(this.playerFile.getGameMode());
        player.setHealth(this.playerFile.getHealth());
        player.setRemainingAir(this.playerFile.getAir());
        player.setFlying(false);
        player.setAllowFlight(false);

        this.teleportToLastLeaveLocation();

        Bukkit.getScheduler().runTaskAsynchronously(
                MSUtils.getInstance(),
                () -> sendJoinMessage(this)
        );
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
            this.playerFile.setFirstJoin(System.currentTimeMillis());
        }

        this.playerFile.save();
        ChatUtils.sendFine(
                text("Создан файл с данными игрока : \"")
                .append(text(this.nickname))
                .append(text("\" с названием : \""))
                .append(text(this.offlinePlayer.getUniqueId().toString()))
                .append(text(".yml\""))
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
            @NotNull String title,
            @NotNull String reason
    ) {
        Player player = this.getOnlinePlayer();

        if (
                player == null
                || !player.isOnline()
                || player.getPlayer() == null
        ) return false;

        this.savePlayerDataParams();
        player.kick(
                Component.empty()
                .append(text(title).color(NamedTextColor.RED).decorate(TextDecoration.BOLD))
                .append(text("\n\n<---====+====--->\n", NamedTextColor.DARK_GRAY))
                .append(text("\nПричина :\n\"")
                .append(text(reason)
                .append(text("\"")))
                .color(NamedTextColor.GRAY))
                .append(text("\n\n<---====+====--->\n", NamedTextColor.DARK_GRAY))
        );
        return true;
    }

    public void setSitting(
            @Nullable Location sitLocation,
            String @Nullable ... args
    ) {
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

            if (args == null || args.length == 0) {
                sendRPEventMessage(player, text(this.playerFile.getPronouns().getSitMessage()), ME);
            } else {
                sendRPEventMessage(player, text(extractMessage(args, 0)), text("приседая"), TODO);
            }
        } else if (sitLocation == null && getConfigCache().seats.containsKey(player)) {
            ArmorStand armorStand = getConfigCache().seats.remove(player);
            Location playerLoc = player.getLocation();
            Location getUpLocation = armorStand.getLocation().add(0.0d, 1.7d, 0.0d);

            getUpLocation.setYaw(playerLoc.getYaw());
            getUpLocation.setPitch(playerLoc.getPitch());
            armorStand.remove();
            player.teleport(getUpLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
            sendRPEventMessage(player, text(this.playerFile.getPronouns().getUnSitMessage()), ME);
        }
    }

    public void unsetSitting(String @Nullable ... args) {
        this.setSitting(null, args);
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
            this.kickPlayer("Вы были кикнуты", "Вас удалили из белого списка");
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
