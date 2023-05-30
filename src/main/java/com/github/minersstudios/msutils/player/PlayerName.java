package com.github.minersstudios.msutils.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.minersstudios.mscore.utils.ChatUtils.createDefaultStyledText;
import static com.github.minersstudios.msutils.utils.MessageUtils.Colors.*;

@SuppressWarnings("unused")
public class PlayerName {
	private @NotNull Name nickname;
	private @NotNull Name firstName;
	private @NotNull Name lastName;
	private @NotNull Name patronymic;

	protected PlayerName(
			@NotNull Name nickname,
			@NotNull Name firstName,
			@NotNull Name lastName,
			@NotNull Name patronymic
	) {
		this.nickname = nickname;
		this.firstName = firstName;
		this.lastName = lastName;
		this.patronymic = patronymic;
	}

	@Contract("_, _, _, _ -> new")
	public static @NotNull PlayerName create(
			@NotNull String nickname,
			@NotNull String firstName,
			@NotNull String lastName,
			@NotNull String patronymic
	) {
		return new PlayerName(
				Name.create(nickname),
				Name.create(firstName),
				Name.create(lastName),
				Name.create(patronymic)
		);
	}

	public @NotNull Component createComponent(
			int id,
			@Nullable TextColor first,
			@Nullable TextColor second
	) {
		return Component.text("[")
				.append(Component.text(id)
				.append(Component.text("] ")))
				.color(first)
				.append(Component.text(this.getFirstName())
				.append(this.getLastName().isEmpty() ? Component.empty() : Component.space()
				.append(Component.text(this.getLastName())))
				.color(second));
	}

	public @NotNull Component createDefaultName(int id) {
		return this.createComponent(id, null, null);
	}

	public @NotNull Component createGoldenName(int id) {
		return this.createComponent(id, JOIN_MESSAGE_COLOR_SECONDARY, JOIN_MESSAGE_COLOR_PRIMARY);
	}

	public @NotNull Component createGrayIDGoldName(int id) {
		return this.createComponent(id, NamedTextColor.GRAY, RP_MESSAGE_MESSAGE_COLOR_PRIMARY);
	}

	public @NotNull Component createGrayIDGreenName(int id) {
		return this.createComponent(id, NamedTextColor.GRAY, NamedTextColor.GREEN);
	}

	public @NotNull String getNickname() {
		return this.nickname.getString();
	}

	public @NotNull Component getNicknameComponent() {
		return this.nickname.getComponent();
	}

	public void setNickname(@NotNull String nickname) {
		this.nickname = Name.create(nickname);
	}

	public @NotNull String getFirstName() {
		return this.firstName.getString();
	}

	public @NotNull Component getFirstNameComponent() {
		return this.firstName.getComponent();
	}

	public void setFirstName(@NotNull String firstName) {
		this.firstName = Name.create(firstName);
	}

	public @NotNull String getLastName() {
		return this.lastName.getString();
	}

	public @NotNull Component getLastNameComponent() {
		return this.lastName.getComponent();
	}

	public void setLastName(@NotNull String lastName) {
		this.lastName = Name.create(lastName);
	}

	public @NotNull String getPatronymic() {
		return this.patronymic.getString();
	}

	public @NotNull Component getPatronymicComponent() {
		return this.patronymic.getComponent();
	}

	public void setPatronymic(@NotNull String patronymic) {
		this.patronymic = Name.create(patronymic);
	}

	public static class Name {
		protected @NotNull String string;
		protected @NotNull Component component;

		protected Name(
				@NotNull String string,
				@NotNull Component component
		) {
			this.string = string;
			this.component = component;
		}

		@Contract("_ -> new")
		public static @NotNull Name create(@NotNull String string) {
			return new Name(
					string,
					createDefaultStyledText(string)
			);
		}

		public void setName(@NotNull String string) {
			this.string = string;
			this.component = createDefaultStyledText(string);
		}

		public @NotNull String getString() {
			return this.string;
		}

		public void setString(@NotNull String string) {
			this.string = string;
		}

		public @NotNull Component getComponent() {
			return this.component;
		}

		public void setComponent(@NotNull Component component) {
			this.component = component;
		}
	}
}
