package github.minersStudios.msUtils.classes;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.enums.ResourcePackType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class RegistrationProcess {
	private Player player;
	private PlayerInfo playerInfo;
	private Location playerLocation;
	private static final String regex = "[-А-яҐґІіЇїЁё]+";

	public void registerPlayer(@Nonnull PlayerInfo playerInfo) {
		player = playerInfo.getOnlinePlayer();

		assert player != null

		playerLocation = player.getLocation();
		player.playSound(playerLocation, Sound.MUSIC_DISC_FAR, 0.15f, 1.25f);	// Не працює
		playerInfo.createPlayerDataFile();

		sendDialogueMessage("Оу...", 100L);
		sendDialogueMessage("Крайне странное местечко.", 150L);
		sendDialogueMessage("Ничего не напоминает?", 225L);
		sendDialogueMessage("Ну ладно...", 300L);
		sendDialogueMessage("Где-то я уже тебя видел", 350L);
		sendDialogueMessage("Напомни-ка своё имя", 400L);
		sendDialogueMessage("Только говори честно, иначе буду тебя ошибочно называть до конца дней своих", 450L);

		Bukkit.getScheduler().runTaskLater(Main.plugin, this::setFirstname, 550L);
	}

	//	NAME/name to Name
	private String strNormalize(String str) {
		assert str != null;

		String capital_letter = str.substring(0, 1),toUpperCase();
		String temp = str.substring(1).toLowerCase();

		str = capital_letter+temp;
		return str;
	}

	private int setFirstname() {
		SignMenu menu = new SignMenu(Arrays.asList("", "---===+===---", "Введите ваше", "имя")).response(player, strings) -> {
			if (strings[0].matches(regex)) {
				String firstname = strings[0].strNormalize();
				playerInfo.setFirstname(firstname);

				sendDialogueMessage("Интересно. . .", 25L);
				sendDialogueMessage("За свою жизнь, я многих повидал с таким именем,", 100L);
				sendDialogueMessage("но тебя вижу впервые.", 225L);
				sendDialogueMessage("Можешь, пожалуйста, уточнить свою фамилию и отчество?", 300L);

				Bukkit.getScheduler().runTaskLater(Main.plugin, this::setLastname, 375L);
				return 0;
			}

			sendWarningMessage();
			menu.open(player);
			return 1;
		});
	}

	private int setLastname() {
		SignMenu menu = new SignMenu(Arrays.asList("", "---===+===---", "Введите вашу", "фамилию")).response((player, strings) -> {
			if (strings[0].matches(regex)) {
				String lastname = strings[0].strNormalize();
				playerInfo.setLastName(lastname);
				Bukkit.getScheduler().runTaskLater(Main.plugin, this::setPatronymic, 10L);
				return 0;
			}

			sendWarningMessage();
			menu.open(player);
			return 1;
		});
	}

	private int setPatronymic() {
		SignMenu menu = new SignMenu(Arrays.asList("", "---===+===---", "Введите ваше", "отчество")).response((player, strings) -> {
			if (strings[0].matches(regex)) {
				String patronymic = strings[0].strNormalize();
				playerInfo.setPatronymic(patronymic);

				player_ID = playerInfo.GetID();
				firstname = playerInfo.getFirstname();
				lastname = playerInfo.getLastname()
				patronymic = playerInfo.getPatronymic()

				sendDialogueMessage(
					"Ну вот и отлично, "
						+ ChatColor.GRAY + "[" + playerID + "] "
						+ ChatColor.WHITE + firstname + " "
						+ lastname + " "
						+ patronymic, 25L);
				sendDialogueMessage("Слушай,", 100L);
				sendDialogueMessage("а как мне к тебе обращаться?", 150L);

				Bukkit.getScheduler().runTaskLater(Main.plugin, () -> player.openInventory(Pronouns.getInventory()), 225L);
				return 0;
			}

			sendWarningMessage();
			menu.open(player);
			return 1;
		});
	}

	public void setPronouns(@Nonnull PlayerInfo playerInfo) {
		playerInfo = playerInfo;
		player = playerInfo.getOnlinePlayer();

		sendDialogueMessage("Славно", 25L);
		sendDialogueMessage("Ну что же...", 75L);
		sendDialogueMessage("Мне уже пора", 125L);
		sendDialogueMessage("Хорошей " + playerInfo.getPronouns().getPronouns() + " дороги, " + playerInfo.getPronouns().getTraveler(), 175L);

		Bukkit.getScheduler().runTaskLater(Main.plugin, this::setOther, 225L);
	}

	private int setOther() {
		player.setDisplayName(playerInfo.getDefaultName());

		if (playerInfo.getResourcePackType() == null) {
			Bukkit.getScheduler().runTask(Main.plugin, () -> player.openInventory(ResourcePackType.getInventory()));
			return 0;
		}

		if (playerInfo.getResourcePackType() == ResourcePackType.NONE) {
			Bukkit.getScheduler().runTask(Main.plugin, playerInfo::teleportToLastLocation);
			return 0;
		}
		ResourcePackType.setResourcePack(playerInfo);
	}

	private void sendWarningMessage() {
		player.sendMessage(ChatColor.GOLD + "Используйте только кириллицу без пробелов!");
	}

	private void sendDialogueMessage(@Nonnull String message, long delay) {
		Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
			player.sendMessage(net.md_5.bungee.api.ChatColor.of("#aba494") + " [0] Незнакомец : " + net.md_5.bungee.api.ChatColor.of("#f2f0e3") + message);
			player.playSound(playerLocation, Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 0.5f, 1.5f);
		}, delay);
	}
}
