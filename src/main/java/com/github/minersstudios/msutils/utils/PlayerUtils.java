package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.google.common.base.Charsets;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;

import static com.github.minersstudios.mscore.utils.ChatUtils.extractMessage;
import static com.github.minersstudios.msutils.MSUtils.getConfigCache;

public final class PlayerUtils {

	/**
	 * Gets UUID from player nickname
	 *
	 * @param nickname player nickname
	 * @return player UUID
	 */
	@Nullable
	public static UUID getUUID(@NotNull String nickname) {
		boolean isOnlineMode;
		try (InputStream input = new FileInputStream("server.properties")) {
			Properties properties = new Properties();
			properties.load(input);
			input.close();
			isOnlineMode = Boolean.parseBoolean(properties.getProperty("online-mode"));
		} catch (IOException e) {
			throw new SecurityException(e);
		}
		if (isOnlineMode) {
			try {
				String UUIDJson = IOUtils.toString(new URL("https://api.mojang.com/users/profiles/minecraft/" + nickname), Charset.defaultCharset());
				if (UUIDJson.isEmpty()) return null;
				return UUID.fromString(((JSONObject) JSONValue.parseWithException(UUIDJson)).get("id").toString().replaceFirst(
						"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
						"$1-$2-$3-$4-$5"
				));
			} catch (IOException | ParseException e) {
				throw new RuntimeException(e);
			}
		} else {
			return UUID.nameUUIDFromBytes(("OfflinePlayer:" + nickname).getBytes(Charsets.UTF_8));
		}
	}

	/**
	 * Gets offline player by nickname
	 *
	 * @param nickname player nickname
	 * @return offline player
	 */
	public static @Nullable OfflinePlayer getOfflinePlayerByNick(@NotNull String nickname) {
		UUID UUID = PlayerUtils.getUUID(nickname);
		return UUID != null ? Bukkit.getOfflinePlayer(UUID) : null;
	}

	/**
	 * Removes player from whitelist
	 *
	 * @param offlinePlayer offline player
	 * @param nickname      player nickname
	 * @return True if player successfully removed from whitelist
	 */
	public static boolean removePlayerFromWhitelist(@NotNull OfflinePlayer offlinePlayer, @Nullable String nickname) {
		if (!Bukkit.getWhitelistedPlayers().contains(offlinePlayer)) return false;
		if (nickname == null) {
			offlinePlayer.setWhitelisted(false);
		} else {
			Bukkit.getScheduler().callSyncMethod(MSUtils.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "minecraft:whitelist remove " + nickname));
		}
		if (offlinePlayer.isOnline() && offlinePlayer.getPlayer() != null) {
			PlayerUtils.kickPlayer(offlinePlayer, "Вы были кикнуты", "Вас удалили из белого списка");
		}
		return true;
	}

	/**
	 * Adds player to whitelist
	 *
	 * @param offlinePlayer offline player
	 * @param nickname      player nickname
	 * @return True if player successfully added to whitelist
	 */
	public static boolean addPlayerToWhitelist(@NotNull OfflinePlayer offlinePlayer, @NotNull String nickname) {
		if (Bukkit.getWhitelistedPlayers().contains(offlinePlayer)) return false;
		try {
			Bukkit.getScheduler().callSyncMethod(MSUtils.getInstance(), () ->
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "minecraft:whitelist add " + nickname)
			);
		} catch (CommandException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	/**
	 * Kicks the player
	 *
	 * @param offlinePlayer offline player
	 * @param reason        kick reason
	 * @return True if player successfully kicked
	 */
	public static boolean kickPlayer(@NotNull OfflinePlayer offlinePlayer, @NotNull String title, @NotNull String reason) {
		if (!offlinePlayer.isOnline() || offlinePlayer.getPlayer() == null) return false;
		new PlayerInfo(offlinePlayer.getUniqueId()).setLastLeaveLocation();
		offlinePlayer.getPlayer().kick(
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

	/**
	 * Sits the player
	 *
	 * @param player      seated player
	 * @param sitLocation location where the player will sit
	 * @param args        player comment ({player-sit-massage} {args})
	 */
	public static void setSitting(@NotNull Player player, @Nullable Location sitLocation, String @Nullable [] args) {
		if (player.getVehicle() != null && player.getVehicle().getType() != EntityType.ARMOR_STAND) return;
		if (!getConfigCache().seats.containsKey(player) && sitLocation != null) {
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
				ChatUtils.sendRPEventMessage(player, Component.text(new PlayerInfo(player.getUniqueId()).getPronouns().getSitMessage()), ChatUtils.RolePlayActionType.ME);
			} else {
				ChatUtils.sendRPEventMessage(player, Component.text(extractMessage(args, 0)), Component.text("приседая"), ChatUtils.RolePlayActionType.TODO);
			}
		} else if (sitLocation == null && getConfigCache().seats.containsKey(player)) {
			ArmorStand armorStand = getConfigCache().seats.remove(player);
			Location playerLoc = player.getLocation();
			Location getUpLocation = armorStand.getLocation().add(0.0d, 1.7d, 0.0d);
			getUpLocation.setYaw(playerLoc.getYaw());
			getUpLocation.setPitch(playerLoc.getPitch());
			armorStand.remove();
			player.teleport(getUpLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
			ChatUtils.sendRPEventMessage(player, Component.text(new PlayerInfo(player.getUniqueId()).getPronouns().getUnSitMessage()), ChatUtils.RolePlayActionType.ME);
		}
	}

	/**
	 * Gets the date at the address
	 *
	 * @param date    date to be converted
	 * @param address address
	 * @return string date format
	 */
	public static @NotNull String getDate(@NotNull Date date, @Nullable InetAddress address) {
		Instant milli = Instant.ofEpochMilli(date.getTime());
		ZoneId zoneId = ZoneId.systemDefault();
		if (address == null) {
			return milli.atZone(zoneId).format(MSCore.getConfigCache().timeFormatter);
		}
		String timeZone = PlayerUtils.getTimezone(address);
		return milli.atZone(
				ZoneId.of(timeZone.equalsIgnoreCase("Europe/Kyiv")
				? "Europe/Kiev"
				: timeZone
		)).format(MSCore.getConfigCache().timeFormatter);
	}

	/**
	 * Gets date with sender time zone
	 *
	 * @param date   date to be converted
	 * @param sender sender (can be player)
	 * @return string date format
	 */
	public static @NotNull String getDate(@NotNull Date date, CommandSender sender) {
		if (sender instanceof Player player) {
			return getDate(date, player.getAddress() != null ? player.getAddress().getAddress() : null);
		}
		return getDate(date, (InetAddress) null);
	}

	/**
	 * Gets timezone from ip
	 *
	 * @param ip IP address
	 * @return timezone from ip
	 */
	public static @NotNull String getTimezone(@NotNull InetAddress ip) {
		try (InputStream input = new URL("http://ip-api.com/json/" + ip.getHostAddress()).openStream()) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			StringBuilder entirePage = new StringBuilder();
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				entirePage.append(inputLine);
			}
			reader.close();
			input.close();
			return entirePage.toString().contains("\"timezone\":\"")
					? entirePage.toString().split("\"timezone\":\"")[1].split("\",")[0]
					: ZoneId.systemDefault().toString();
		} catch (IOException e) {
			MSUtils.getInstance().getLogger().log(Level.WARNING, e.getMessage());
			return ZoneId.systemDefault().toString();
		}
	}

	/**
	 * @param offlinePlayer player
	 * @return True if the player isn't in dark_world and hasn't vanished
	 */
	@Contract("null -> false")
	public static boolean isOnline(@Nullable OfflinePlayer offlinePlayer) {
		return isOnline(offlinePlayer, false);
	}

	/**
	 * @param offlinePlayer player
	 * @param ignoreWorld   ignore world_dark check
	 * @return True if the player isn't in dark_world and hasn't vanished
	 */
	@Contract("null, _ -> false")
	public static boolean isOnline(@Nullable OfflinePlayer offlinePlayer, boolean ignoreWorld) {
		if (offlinePlayer == null) return false;
		Player player = offlinePlayer.getPlayer();
		return player != null
				&& (ignoreWorld || player.getWorld() != MSUtils.getWorldDark())
				&& !isVanished(player);
	}

	public static boolean isVanished(@NotNull Player player) {
		return player.getMetadata("vanished").stream().anyMatch(MetadataValue::asBoolean);
	}

	public static @NotNull Map<@NotNull EquipmentSlot, @Nullable ItemStack> getPlayerEquippedItems(@NotNull PlayerInventory inventory) {
		Map<EquipmentSlot, ItemStack> playerEquippedItems = new HashMap<>();
		playerEquippedItems.put(EquipmentSlot.HEAD, inventory.getHelmet());
		playerEquippedItems.put(EquipmentSlot.CHEST, inventory.getChestplate());
		playerEquippedItems.put(EquipmentSlot.LEGS, inventory.getLeggings());
		playerEquippedItems.put(EquipmentSlot.FEET, inventory.getBoots());
		return playerEquippedItems;
	}
}
