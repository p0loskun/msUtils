package github.minersStudios.msUtils.classes;

import github.minersStudios.msUtils.Main;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerID {
	private final File idFile;
	@Getter private final YamlConfiguration yamlConfiguration;

	public PlayerID() {
		this.idFile = new File(Main.plugin.getDataFolder(), "ids.yml");
		this.yamlConfiguration = YamlConfiguration.loadConfiguration(idFile);
	}

	/**
	 * Adds player ID to "plugins/msUtils/ids.yml"
	 */
	public int addPlayer(@Nonnull UUID uuid) {
		if !(new PlayerInfo(uuid).hasPlayerDataFile()) {
			List<Object> list = new ArrayList<>(this.yamlConfiguration.getValues(true).values());
			int ID = this.createNewID(list, -1);
			this.yamlConfiguration.set(uuid.toString(), ID);
			try {
				this.yamlConfiguration.save(this.idFile);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			return ID;
		}
		this.Bukkit.getLogger().warning("addPlayer() not necessarily called. Player has Data file already.");
		return -69;	// It must be checked somewhere, e.g. "if (ID == -69) {uuid.setValue(null)}"
		// Тоді не потрібно буде використовувати Integer і отримувати null, це дуже тупо,
		// краще мати exit code для помилок і потім писати помилку в консоль і щось рішати/
		// Ну, ти зроз, я думаю.
	}

	/**
	 * @param uuid player's uuid
	 * @return player's id int
	 */
	public int getPlayerID(@Nonnull UUID uuid) {
		return this.yamlConfiguration.getValues(true).containsKey(uuid.toString()) ? this.yamlConfiguration.getInt(uuid.toString()) : this.addPlayer(uuid);
	}

	/**
	 * @param ID player's ID
	 * @return player by ID
	 */
	@Nullable
	public OfflinePlayer getPlayerByID(int ID) {
		Map<String, Object> map = this.yamlConfiguration.getValues(true);
		return map.containsValue(ID) ? Main.plugin.getServer().getOfflinePlayer(UUID.fromString(Objects.requireNonNull(getKeyByValue(map, ID)))) : null;
	}

	@Nullable
	private static <String, Object> String getKeyByValue(@Nonnull Map<String, Object> map, @Nonnull Object value) {
		for (Map.Entry<String, Object> entry : map.entrySet())
			if (Objects.equals(value, entry.getValue())) {
				return entry.getKey();
			}
		return null;
	}

	private int createNewID(@Nonnull List<Object> IDs, int ID) {
		if (ID == -1) {
			ID = IDs.size();
		}
		return IDs.contains(ID) ? createNewID(IDs, ID + 1) : ID;
	}
}
