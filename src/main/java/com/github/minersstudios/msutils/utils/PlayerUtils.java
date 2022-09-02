package com.github.minersstudios.msutils.utils;

import com.google.common.base.Charsets;
import com.github.minersstudios.msutils.Main;
import com.github.minersstudios.msutils.classes.PlayerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.io.IOUtils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerUtils {
	private static final Map<Player, ArmorStand> seats = new ConcurrentHashMap<>();

	/**
	 * Gets UUID from player nickname
	 *
	 * @param nickname player nickname
	 * @return player UUID
	 */
	@Nullable
	public static UUID getUUID(@Nonnull String nickname) {
		boolean isOnlineMode = true;
		try (InputStream input = new FileInputStream("server.properties")) {
			Properties properties = new Properties();
			properties.load(input);
			isOnlineMode = Boolean.parseBoolean(properties.getProperty("online-mode"));
		} catch (IOException e) {
			e.printStackTrace();
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
				e.printStackTrace();
			}
		} else {
			return UUID.nameUUIDFromBytes(("OfflinePlayer:" + nickname).getBytes(Charsets.UTF_8));
		}
		return null;
	}

	/**
	 * Gets offline player by nickname
	 *
	 * @param nickname player nickname
	 * @return offline player
	 */
	@Nullable
	public static OfflinePlayer getOfflinePlayerByNick(@Nonnull String nickname) {
		UUID UUID = getUUID(nickname);
		return UUID != null ? Bukkit.getOfflinePlayer(UUID) : null;
	}

	/**
	 * Removes player from whitelist
	 *
	 * @param offlinePlayer offline player
	 * @param nickname      player nickname
	 * @return True if player successfully removed from whitelist
	 */
	public static boolean removePlayerFromWhitelist(@Nonnull OfflinePlayer offlinePlayer, @Nullable String nickname) {
		if (!Bukkit.getWhitelistedPlayers().contains(offlinePlayer)) return false;
		if (nickname == null) {
			offlinePlayer.setWhitelisted(false);
		} else {
			Bukkit.getScheduler().callSyncMethod(Main.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "minecraft:whitelist remove " + nickname));
		}
		if (offlinePlayer.isOnline() && offlinePlayer.getPlayer() != null) {
			kickPlayer(offlinePlayer, "Вы были кикнуты", "Вас удалили из белого списка");
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
	public static boolean addPlayerToWhitelist(@Nonnull OfflinePlayer offlinePlayer, @Nonnull String nickname) {
		if (Bukkit.getWhitelistedPlayers().contains(offlinePlayer)) return false;
		try {
			Bukkit.getScheduler().callSyncMethod(Main.getInstance(), () ->
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "minecraft:whitelist add " + nickname)
			);
		} catch (Exception e) {
			e.printStackTrace();
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
	public static boolean kickPlayer(@Nonnull OfflinePlayer offlinePlayer, @Nonnull String title, @Nonnull String reason) {
		if (!offlinePlayer.isOnline() || offlinePlayer.getPlayer() == null) return false;
		PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
		playerInfo.setLastLeaveLocation();
		playerInfo.setHealth(offlinePlayer.getPlayer().getHealth());
		offlinePlayer.getPlayer().kick(
				Component.text("")
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

	public static boolean setSitting(@Nonnull Player player, @Nullable Location sitLocation, @Nullable String... args) {
		if (player.getVehicle() != null && player.getVehicle().getType() != EntityType.ARMOR_STAND) return true;
		if (!seats.containsKey(player) && sitLocation != null) {
			player.getWorld().spawn(sitLocation.clone().subtract(0.0d, 1.7d, 0.0d), ArmorStand.class, (armorStand) -> {
				armorStand.setGravity(false);
				armorStand.setVisible(false);
				armorStand.setCollidable(false);
				armorStand.addPassenger(player);
				armorStand.addScoreboardTag("customDecor");
				seats.put(player, armorStand);
			});
			return args != null
					? ChatUtils.sendRPEventMessage(player, Component.text(ChatUtils.extractMessage(0, args)), Component.text("приседая"), ChatUtils.RolePlayActionType.TODO)
					: ChatUtils.sendRPEventMessage(player, Component.text(new PlayerInfo(player.getUniqueId()).getPronouns().getSitMessage()), ChatUtils.RolePlayActionType.ME);
		} else if (sitLocation == null && seats.containsKey(player)) {
			ArmorStand armorStand = seats.remove(player);
			Location getUpLocation = armorStand.getLocation().add(0.0d, 2.0d, 0.0d);
			getUpLocation.setYaw(player.getLocation().getYaw());
			getUpLocation.setPitch(player.getLocation().getPitch());
			player.teleport(getUpLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
			armorStand.remove();
			ChatUtils.sendRPEventMessage(player, Component.text(new PlayerInfo(player.getUniqueId()).getPronouns().getUnSitMessage()), ChatUtils.RolePlayActionType.ME);
		}
		return true;
	}

	@Nonnull
	public static String getTimezone(@Nonnull InetSocketAddress ip) {
		try (InputStream input = new URL("http://ip-api.com/json/" + ip.getHostName()).openStream()) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			StringBuilder entirePage = new StringBuilder();
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				entirePage.append(inputLine);
			}
			reader.close();
			return entirePage.toString().contains("\"timezone\":\"")
					? entirePage.toString().split("\"timezone\":\"")[1].split("\",")[0]
					: ZoneId.systemDefault().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ZoneId.systemDefault().toString();
	}

	public static Map<Player, ArmorStand> getSeats() {
		return seats;
	}
}
