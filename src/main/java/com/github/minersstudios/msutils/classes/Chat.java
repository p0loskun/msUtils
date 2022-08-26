package com.github.minersstudios.msutils.classes;

import net.kyori.adventure.text.format.TextColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public record Chat(
		boolean isEnabled,
		double getRadius,
		@Nonnull List<String> getPermissions,
		@Nullable TextColor getPrimaryColor,
		@Nullable TextColor getSecondaryColor,
		boolean isDiscordEnabled,
		@Nullable List<String> getDiscordChannelIds,
		@Nullable String getSymbol,
		boolean isEnableChatSymbols
) {}


