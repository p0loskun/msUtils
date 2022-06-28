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
		this.playerInfo = playerInfo;
		this.player = playerInfo.getOnlinePlayer();
		assert this.player != null;
		this.playerLocation = this.player.getLocation();
		this.player.playSound(this.playerLocation, Sound.MUSIC_DISC_FAR, 0.15f, 1.25f);
		playerInfo.createPlayerDataFile();

		this.sendDialogueMessage("Оу...", 100L);
		this.sendDialogueMessage("Крайне странное местечко", 150L);
		this.sendDialogueMessage("Ничего не напоминает?", 225L);
		this.sendDialogueMessage("Ну ладно...", 300L);
		this.sendDialogueMessage("Где-то я уже тебя видел", 350L);
		this.sendDialogueMessage("Напомни-ка своё имя", 400L);
		this.sendDialogueMessage("Только говори честно, иначе буду тебя ошибочно называть до конца дней своих", 450L);

		Bukkit.getScheduler().runTaskLater(Main.plugin, this::setFirstname, 550L);
	}

	private void setFirstname() {
		SignMenu menu = new SignMenu(Arrays.asList("", "---===+===---", "Введите ваше", "имя")).response((player, strings) -> {
			if (strings[0].matches(regex)) {
				String firstname = strNormalize(strings[0]);
				this.playerInfo.setFirstname(firstname);

				this.sendDialogueMessage("Интересно. . .", 25L);
				this.sendDialogueMessage("За свою жизнь, я многих повидал с таким именем,", 100L);
				this.sendDialogueMessage("но тебя вижу впервые", 225L);
				this.sendDialogueMessage("Можешь, пожалуйста, уточнить свою фамилию и отчество?", 300L);

				Bukkit.getScheduler().runTaskLater(Main.plugin, this::setLastname, 375L);
				return true;
			}
			return this.sendWarningMessage();
		});
		menu.open(this.player);
	}

	private void setLastname() {
		SignMenu menu = new SignMenu(Arrays.asList("", "---===+===---", "Введите вашу", "фамилию")).response((player, strings) -> {
			if (strings[0].matches(regex)) {
				String lastname = strNormalize(strings[0]);
				this.playerInfo.setLastName(lastname);
				Bukkit.getScheduler().runTaskLater(Main.plugin, this::setPatronymic, 10L);
				return true;
			}
			return this.sendWarningMessage();
		});
		menu.open(this.player);
	}

	private void setPatronymic() {
		SignMenu menu = new SignMenu(Arrays.asList("", "---===+===---", "Введите ваше", "отчество")).response((player, strings) -> {
			if (strings[0].matches(regex)) {
				String patronymic = strNormalize(strings[0]);
				this.playerInfo.setPatronymic(patronymic);

				this.sendDialogueMessage(
					"Ну вот и отлично, "
					+ ChatColor.GRAY + "[" + this.playerInfo.getID() + "] "
					+ ChatColor.WHITE + this.playerInfo.getFirstname() + " "
					+ this.playerInfo.getLastname() + " "
					+ this.playerInfo.getPatronymic(), 25L);
				this.sendDialogueMessage("Слушай, а", 100L);
				this.sendDialogueMessage("как мне к тебе обращаться?", 150L);

				Bukkit.getScheduler().runTaskLater(Main.plugin, () -> this.player.openInventory(Pronouns.getInventory()), 225L);
				return true;
			}
			return this.sendWarningMessage();
		});
		menu.open(this.player);
	}

	public void setPronouns(@Nonnull PlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
		this.player = playerInfo.getOnlinePlayer();

		this.sendDialogueMessage("Славно", 25L);
		this.sendDialogueMessage("Ну что же...", 75L);
		this.sendDialogueMessage("Мне уже пора", 125L);
		this.sendDialogueMessage("Хорошей " + playerInfo.getPronouns().getPronouns() + " дороги, " + playerInfo.getPronouns().getTraveler(), 175L);

		Bukkit.getScheduler().runTaskLater(Main.plugin, this::setOther, 225L);
	}

	private void setOther() {
		this.player.setDisplayName(this.playerInfo.getDefaultName());
		if (this.playerInfo.getResourcePackType() == null) {
			Bukkit.getScheduler().runTask(Main.plugin, () -> this.player.openInventory(ResourcePackType.getInventory()));
		} else if (this.playerInfo.getResourcePackType() == ResourcePackType.NONE) {
			Bukkit.getScheduler().runTask(Main.plugin, this.playerInfo::teleportToLastLeaveLocation);
		} else {
			ResourcePackType.setResourcePack(this.playerInfo);
		}
	}

	private boolean sendWarningMessage() {
		this.player.sendMessage(ChatColor.GOLD + "Используйте только кириллицу, без пробелов!");
		return false;
	}

	private void sendDialogueMessage(@Nonnull String message, long delay) {
		Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
			this.player.sendMessage(net.md_5.bungee.api.ChatColor.of("#aba494") + "[0] Незнакомец : " + net.md_5.bungee.api.ChatColor.of("#f2f0e3") + message);
			this.player.playSound(this.playerLocation, Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 0.5f, 1.5f);
		}, delay);
	}

	@Nonnull
	private static String strNormalize(@Nonnull String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}
}
