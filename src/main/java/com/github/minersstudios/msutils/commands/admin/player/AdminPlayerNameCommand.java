package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerFile;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.PlayerName;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static net.kyori.adventure.text.Component.text;

public class AdminPlayerNameCommand {

	public static boolean runCommand(
			@NotNull CommandSender sender,
			String @NotNull [] args,
			@NotNull PlayerInfo playerInfo
	) {
		PlayerFile playerFile = playerInfo.getPlayerFile();
		YamlConfiguration yamlConfiguration = playerFile.getYamlConfiguration();
		PlayerName playerName = playerFile.getPlayerName();
		boolean haveArg = args.length >= 4;
		String paramString = args.length >= 3 ? args[2].toLowerCase(Locale.ENGLISH) : "";
		String paramArgString = haveArg ? args[3].toLowerCase(Locale.ENGLISH) : "";

		if (args.length == 2) {
			ChatUtils.sendFine(sender,
					text("Полное имя игрока :\n")
					.append(text("    "))
					.append(playerInfo.getGrayIDGreenName())
					.appendSpace()
					.append(text(playerName.getPatronymic(), NamedTextColor.GREEN))
			);
			return true;
		}

		if (
				!paramArgString.isEmpty()
				&& !"empty".equals(paramArgString)
				&& !MSPlayerUtils.matchesNameRegex(paramArgString)
		) {
			ChatUtils.sendError(sender, "Введите показатель в правильном формате");
			return true;
		}

		switch (paramString) {
			case "reset" -> {
				if (haveArg) return false;

				ChatUtils.sendFine(sender,
						text("Полное имя игрока : ")
						.append(playerInfo.getGrayIDGreenName())
						.appendNewline()
						.append(text("    Было успешно сброшено"))
				);

				yamlConfiguration.set("name.first-name", null);
				yamlConfiguration.set("name.last-name", null);
				yamlConfiguration.set("name.patronymic", null);
				yamlConfiguration.set("pronouns", null);
				playerFile.save();
				playerInfo.initNames();
				playerInfo.kickPlayer(
						"Вы были кикнуты!",
						"Ваше полное имя было сброшено"
				);
				return true;
			}
			case "first-name" -> {
				if (!haveArg) {
					ChatUtils.sendFine(sender,
							text("Имя игрока равно : ")
							.append(text(playerName.getFirstName()))
					);
					return true;
				}

				playerName.setFirstName(paramArgString);
			}
			case "last-name" -> {
				if (!haveArg) {
					ChatUtils.sendFine(sender,
							text("Фамилия игрока равна : ")
							.append(text(playerName.getLastName()))
					);
					return true;
				}

				playerName.setLastName("empty".equals(paramArgString) ? "" : paramArgString);
			}
			case "patronymic" -> {
				if (!haveArg) {
					ChatUtils.sendFine(sender,
							text("Отчество игрока равно : ")
							.append(text(playerName.getPatronymic()))
					);
					return true;
				}

				playerName.setPatronymic("empty".equals(paramArgString) ? "" : paramArgString);
			}
		}

		playerFile.updateName();
		playerFile.save();
		playerInfo.initNames();
		ChatUtils.sendFine(sender,
				text("Теперь, полное имя игрока выглядит так :\n")
				.append(text("    "))
				.append(playerInfo.getGrayIDGreenName())
				.appendSpace()
				.append(text(playerName.getPatronymic(), NamedTextColor.GREEN))
		);
		return true;
	}
}
