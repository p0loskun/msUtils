package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;

import com.github.minersstudios.msutils.player.PlayerFile;
import com.github.minersstudios.msutils.player.PlayerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class AdminGameParamsCommand {

    public static boolean runCommand(
            @NotNull CommandSender sender,
            String @NotNull [] args,
            @NotNull OfflinePlayer offlinePlayer,
            @NotNull PlayerInfo playerInfo
    ) {
        if (args.length < 3) {
            ChatUtils.sendError(sender, translatable("ms.command.player.game_params.use_one_of"));
            return true;
        }

        PlayerFile playerFile = playerInfo.getPlayerFile();
        boolean haveArg = args.length >= 4;
        String paramString = args[2].toLowerCase(Locale.ROOT);
        String paramArgString = haveArg ? args[3].toLowerCase(Locale.ROOT) : "";
        Player player = offlinePlayer.getPlayer();
        TranslatableComponent wrongFormat = Component.translatable("ms.error.wrong_format");

        switch (paramString) {
            case "game-mode" -> {
                if (!haveArg) {
                    ChatUtils.sendFine(
                            sender,
                            translatable(
                                    player == null ? "ms.command.player.game_params.get.game_mode" : "ms.command.player.game_params.get.game_mode_full",
                                    playerInfo.getGrayIDGreenName(),
                                    text(playerInfo.getNickname()),
                                    text(playerFile.getGameMode().name().toLowerCase(Locale.ROOT)),
                                    player == null ? text() : text(player.getGameMode().name().toLowerCase(Locale.ROOT))
                            )
                    );
                    return true;
                }

                GameMode gameMode;
                try {
                    gameMode = GameMode.valueOf(paramArgString.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException ignore) {
                    ChatUtils.sendError(sender, translatable("ms.command.player.game_params.game_mode_use_one_of"));
                    return true;
                }

                if (player != null) {
                    player.setGameMode(gameMode);
                }

                playerFile.setGameMode(gameMode);
                playerFile.save();
                ChatUtils.sendFine(
                        sender,
                        translatable(
                                "ms.command.player.game_params.set.game_mode",
                                playerInfo.getGrayIDGreenName(),
                                text(playerInfo.getNickname()),
                                text(paramArgString)
                        )
                );
                return true;
            }
            case "health" -> {
                if (!haveArg) {
                    ChatUtils.sendFine(
                            sender,
                            translatable(
                                    player == null ? "ms.command.player.game_params.get.health" : "ms.command.player.game_params.get.health_full",
                                    playerInfo.getGrayIDGreenName(),
                                    text(playerInfo.getNickname()),
                                    text(Double.toString(playerFile.getHealth())),
                                    player == null ? text() : text(Double.toString(player.getHealth()))
                            )
                    );
                    return true;
                }

                double health;
                try {
                    health = Double.parseDouble(paramArgString);
                } catch (NumberFormatException ignore) {
                    ChatUtils.sendError(sender, wrongFormat);
                    return true;
                }

                if (player != null) {
                    player.setHealthScale(health);
                }

                playerFile.setHealth(health);
                playerFile.save();
                ChatUtils.sendFine(
                        sender,
                        translatable(
                                "ms.command.player.game_params.set.health",
                                playerInfo.getGrayIDGreenName(),
                                text(playerInfo.getNickname()),
                                text(paramArgString)
                        )
                );
                return true;
            }
            case "air" -> {
                if (!haveArg) {
                    ChatUtils.sendFine(
                            sender,
                            translatable(
                                    player == null ? "ms.command.player.game_params.get.air" : "ms.command.player.game_params.get.air_full",
                                    playerInfo.getGrayIDGreenName(),
                                    text(playerInfo.getNickname()),
                                    text(playerFile.getAir()),
                                    player == null ? text() : text(player.getRemainingAir())
                            )
                    );
                    return true;
                }
                int air;
                try {
                    air = Integer.parseInt(paramArgString);
                } catch (NumberFormatException ignore) {
                    ChatUtils.sendError(sender, wrongFormat);
                    return true;
                }

                if (player != null) {
                    player.setRemainingAir(air);
                }

                playerFile.setAir(air);
                playerFile.save();
                ChatUtils.sendFine(
                        sender,
                        translatable(
                                "ms.command.player.game_params.set.air",
                                playerInfo.getGrayIDGreenName(),
                                text(playerInfo.getNickname()),
                                text(paramArgString)
                        )
                );
                return true;
            }
        }
        return false;
    }
}
