package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerFile;
import com.github.minersstudios.msutils.player.PlayerInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static net.kyori.adventure.text.Component.text;

public class AdminGameParamsCommand {

    public static boolean runCommand(
            @NotNull CommandSender sender,
            String @NotNull [] args,
            @NotNull OfflinePlayer offlinePlayer,
            @NotNull PlayerInfo playerInfo
    ) {
        if (args.length < 3) {
            ChatUtils.sendError(sender, "Используйте один из доступных вариантов :\n    game-mode, health, air");
            return true;
        }

        PlayerFile playerFile = playerInfo.getPlayerFile();
        boolean haveArg = args.length >= 4;
        String paramString = args[2].toLowerCase(Locale.ENGLISH);
        String paramArgString = haveArg ? args[3].toLowerCase(Locale.ENGLISH) : "";
        Player player = offlinePlayer.getPlayer();

        switch (paramString) {
            case "game-mode" -> {
                if (!haveArg) {
                    ChatUtils.sendFine(sender,
                            text("Режим игры игрока : ")
                            .append(playerInfo.getGrayIDGreenName())
                            .appendNewline()
                            .append(text("    Равен : "))
                            .appendNewline()
                            .append(text("      - (В файле) \""))
                            .append(text(playerFile.getGameMode().name().toLowerCase(Locale.ENGLISH)))
                            .append(text("\""))
                            .append(
                                    player == null
                                            ? text()
                                            : Component.newline()
                                            .append(text("      - (В игре) \""))
                                            .append(text(player.getGameMode().name().toLowerCase(Locale.ENGLISH)))
                                            .append(text("\""))
                            )
                    );
                    return true;
                }

                GameMode gameMode;
                try {
                    gameMode = GameMode.valueOf(paramArgString.toUpperCase(Locale.ENGLISH));
                } catch (IllegalArgumentException ignore) {
                    ChatUtils.sendError(sender, "Используйте один из доступных вариантов :\n    survival, creative, spectator, adventure");
                    return true;
                }

                if (player != null) {
                    player.setGameMode(gameMode);
                }

                playerFile.setGameMode(gameMode);
                playerFile.save();
                ChatUtils.sendFine(sender,
                        text("Режим игры игрока : ")
                        .append(playerInfo.getGrayIDGreenName())
                        .appendNewline()
                        .append(text("    Был успешно изменён на : \""))
                        .append(text(paramArgString))
                        .append(text("\""))
                );
                return true;
            }
            case "health" -> {
                if (!haveArg) {
                    ChatUtils.sendFine(sender,
                            text("Уровень здоровья игрока : ")
                            .append(playerInfo.getGrayIDGreenName())
                            .appendNewline()
                            .append(text("    Равен : "))
                            .appendNewline()
                            .append(text("      - (В файле) "))
                            .append(text(Double.toString(playerFile.getHealth())))
                            .append(
                                    player == null
                                            ? text()
                                            : Component.newline()
                                            .append(text("      - (В игре) "))
                                            .append(text(Double.toString(player.getHealth())))
                            )
                    );
                    return true;
                }

                double health;
                try {
                    health = Double.parseDouble(paramArgString);
                } catch (NumberFormatException ignore) {
                    ChatUtils.sendError(sender, "Введите показатель в правильном формате");
                    return true;
                }

                if (player != null) {
                    player.setHealthScale(health);
                }

                playerFile.setHealth(health);
                playerFile.save();
                ChatUtils.sendFine(sender,
                        text("Уровень здоровья игрока : ")
                        .append(playerInfo.getGrayIDGreenName())
                        .appendNewline()
                        .append(text("    Был успешно изменён на : "))
                        .append(text(Double.toString(health)))
                );
                return true;
            }
            case "air" -> {
                if (!haveArg) {
                    ChatUtils.sendFine(sender,
                            text("Уровень воздуха игрока : ")
                            .append(playerInfo.getGrayIDGreenName())
                            .appendNewline()
                            .append(text("    Равен : "))
                            .appendNewline()
                            .append(text("      - (В файле) "))
                            .append(text(playerFile.getAir()))
                            .append(
                                    player == null
                                            ? text()
                                            : Component.newline()
                                            .append(text("      - (В игре) "))
                                            .append(text(player.getRemainingAir()))
                            )
                    );
                    return true;
                }
                int air;
                try {
                    air = Integer.parseInt(paramArgString);
                } catch (NumberFormatException ignore) {
                    ChatUtils.sendError(sender, "Введите показатель в правильном формате");
                    return true;
                }

                if (player != null) {
                    player.setRemainingAir(air);
                }

                playerFile.setAir(air);
                playerFile.save();
                ChatUtils.sendFine(sender,
                        text("Уровень воздуха игрока : ")
                        .append(playerInfo.getGrayIDGreenName())
                        .appendNewline()
                        .append(text("    Был успешно изменён на : "))
                        .append(text(air))
                );
                return true;
            }
        }
        return false;
    }
}
