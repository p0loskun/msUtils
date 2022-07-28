package github.minersStudios.msUtils.utils;

import com.google.common.base.Charsets;
import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.io.IOUtils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.io.*;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class PlayerUtils {
	@Getter private static final Map<Player, ArmorStand> seats = new HashMap<>();

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
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		if (isOnlineMode) {
			try {
				String UUIDJson = IOUtils.toString(new URL("https://api.mojang.com/users/profiles/minecraft/" + nickname), Charset.defaultCharset());
				if (UUIDJson.isEmpty())
					return null;
				return UUID.fromString(((JSONObject) JSONValue.parseWithException(UUIDJson)).get("id").toString().replaceFirst(
						"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
						"$1-$2-$3-$4-$5"
				));
			} catch (IOException | ParseException exception) {
				exception.printStackTrace();
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
		UUID UUID = PlayerUtils.getUUID(nickname);
		return UUID != null ? Bukkit.getOfflinePlayer(UUID) : null;
	}

	/**
	 * Removes player from whitelist
	 *
	 * @param offlinePlayer offline player
	 * @param nickname player nickname
	 * @return True if player successfully removed from whitelist
	 */
	public static boolean removePlayerFromWhitelist(@Nonnull OfflinePlayer offlinePlayer, @Nullable String nickname) {
		if (!Bukkit.getWhitelistedPlayers().contains(offlinePlayer))
			return false;
		if (nickname == null) {
			offlinePlayer.setWhitelisted(false);
		} else {
			Bukkit.getScheduler().callSyncMethod(Main.plugin, () -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"minecraft:whitelist remove " + nickname));
		}
		if (offlinePlayer.isOnline() && offlinePlayer.getPlayer() != null)
			PlayerUtils.kickPlayer(offlinePlayer, "Вы были кикнуты", "Вас удалили из белого списка");
		return true;
	}

	/**
	 * Adds player to whitelist
	 *
	 * @param offlinePlayer offline player
	 * @param nickname player nickname
	 * @return True if player successfully added to whitelist
	 */
	public static boolean addPlayerToWhitelist(@Nonnull OfflinePlayer offlinePlayer, @Nonnull String nickname) {
		if (Bukkit.getWhitelistedPlayers().contains(offlinePlayer))
			return false;
		try {
			Bukkit.getScheduler().callSyncMethod(Main.plugin, () -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"minecraft:whitelist add " + nickname));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return true;
	}

	/**
	 * Kicks the player
	 *
	 * @param offlinePlayer offline player
	 * @param reason kick reason
	 *
	 * @return True if player successfully kicked
	 */
	public static boolean kickPlayer(@Nonnull OfflinePlayer offlinePlayer, @Nonnull String title, @Nonnull String reason) {
		if (!offlinePlayer.isOnline() || offlinePlayer.getPlayer() == null)
			return false;
		new PlayerInfo(offlinePlayer.getUniqueId()).setLastLeaveLocation();
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

	@Nonnull
	public static String getTimezone(@Nonnull InetSocketAddress ip) {
		try {
			BufferedReader stream = new BufferedReader(new InputStreamReader(new URL("http://ip-api.com/json/" + ip.getHostName()).openStream()));
			StringBuilder entirePage = new StringBuilder();
			String inputLine;
			while((inputLine = stream.readLine()) != null)
				entirePage.append(inputLine);
			stream.close();
			return !entirePage.toString().contains("\"timezone\":\"") ? ZoneId.systemDefault().toString() : entirePage.toString().split("\"timezone\":\"")[1].split("\",")[0];
		} catch (IOException exception) {
			exception.printStackTrace();
			return ZoneId.systemDefault().toString();
		}
	}

	@Nonnull
	public static String encrypt(@Nonnull String input) {
		try {
			StringBuilder hashText = new StringBuilder(new BigInteger(1, MessageDigest.getInstance("SHA-1").digest(input.getBytes())).toString(16));
			while (hashText.length() < 32)
				hashText.insert(0, "0");
			return hashText.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}