package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.CommandUtils;
import com.github.minersstudios.mscore.utils.DateUtils;
import com.github.minersstudios.msutils.player.PlayerFile;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.MuteFileUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Locale;

import static net.kyori.adventure.text.Component.text;

public class AdminMuteInfoCommand {

	public static boolean runCommand(
			@NotNull CommandSender sender,
			String @NotNull [] args,
			@NotNull PlayerInfo playerInfo
	) {
		PlayerFile playerFile = playerInfo.getPlayerFile();
		boolean muted = playerInfo.isMuted();
		boolean haveArg = args.length >= 4;
		String paramString = args.length >= 3 ? args[2].toLowerCase(Locale.ENGLISH) : "";

		if (args.length == 2) {
			ChatUtils.sendFine(sender,
					text("Информация о мьюте игрока : ")
					.append(playerInfo.getGrayIDGreenName())
					.appendNewline()
					.append(
							muted
							? text("    - Причина : \"")
							.append(text(playerFile.getMuteReason()))
							.append(text("\""))
							.appendNewline()
							.append(text("    - Замьючен до : "))
							.append(text(DateUtils.getDate(new Date(playerFile.getMutedTo()), sender)))
							: text("    - Не замьючен")
					)
			);
			return true;
		}

		if (!muted) {
			ChatUtils.sendError(sender,
					text("Данный параметр не может быть изменён/считан, так как игрок : ")
					.append(playerInfo.getGrayIDGreenName())
					.appendNewline()
					.append(text("    - Не замьючен"))
			);
			return true;
		}

		switch (paramString) {
			case "reason" -> {
				if (!haveArg) {
					ChatUtils.sendFine(sender,
							text("Причиной мьюта игрока : ")
							.append(playerInfo.getGrayIDGreenName())
							.appendNewline()
							.append(text("    Является : \""))
							.append(text(playerFile.getMuteReason()))
							.append(text("\""))
					);
					return true;
				}

				String reason = ChatUtils.extractMessage(args, 3);

				playerFile.setMuteReason(reason);
				playerFile.save();
				ChatUtils.sendFine(sender,
						text("Причина мьюта игрока : ")
						.append(playerInfo.getGrayIDGreenName())
						.appendNewline()
						.append(text("    Была успешно изменена на : \""))
						.append(text(reason))
						.append(text("\""))
				);
				return true;
			}
			case "time" -> {
				if (!haveArg) {
					ChatUtils.sendFine(sender,
							text("Крайней датой мьюта игрока : ")
							.append(playerInfo.getGrayIDGreenName())
							.appendNewline()
							.append(text("    Является : "))
							.append(text(DateUtils.getDate(new Date(playerFile.getMutedTo()), sender)))
					);
					return true;
				}

				Date date = CommandUtils.getDateFromString(args[1], false);

				if (date == null) {
					ChatUtils.sendError(sender, "Введите показатель в правильном формате");
					return true;
				}

				long time = date.getTime();

				playerFile.setMutedTo(time);
				playerFile.save();
				MuteFileUtils.addPlayer(playerInfo.getOfflinePlayer(), time);
				ChatUtils.sendFine(sender,
						text("Крайней датой мьюта игрока : ")
						.append(playerInfo.getGrayIDGreenName())
						.appendNewline()
						.append(text("    Стала : "))
						.append(text(DateUtils.getDate(date, sender)))
				);
				return true;
			}
		}
		return false;
	}
}
