package com.github.minersstudios.msutils.player;

import org.jetbrains.annotations.NotNull;

public enum Pronouns {
	HE(
			"зашёл на сервер",
			"вышел из сервера",
			"плюнул",
			"пукнул",
			"тебе",
			"путник",
			"сел",
			"встал",
			"умер",
			"убил",
			"сказал"
	),
	SHE(
			"зашла на сервер",
			"вышла из сервера",
			"плюнула",
			"пукнула",
			"тебе",
			"путница",
			"села",
			"встала",
			"умерла",
			"убила",
			"сказала"
	),
	THEY(
			"зашли на сервер",
			"вышли из сервера",
			"плюнули",
			"пукнули",
			"вам",
			"путник",
			"сели",
			"встали",
			"умерли",
			"убили",
			"сказали"
	);

	private final @NotNull String
			joinMessage,
			quitMessage,
			spitMessage,
			fartMessage,
			pronouns,
			traveler,
			sitMessage,
			unSitMessage,
			deathMessage,
			killMessage,
			saidMessage;

	Pronouns(
			@NotNull String joinMessage,
			@NotNull String quitMessage,
			@NotNull String spitMessage,
			@NotNull String fartMessage,
			@NotNull String pronouns,
			@NotNull String traveler,
			@NotNull String sitMessage,
			@NotNull String unSitMessage,
			@NotNull String deathMessage,
			@NotNull String killMessage,
			@NotNull String saidMessage
	) {
		this.joinMessage = joinMessage;
		this.quitMessage = quitMessage;
		this.spitMessage = spitMessage;
		this.fartMessage = fartMessage;
		this.pronouns = pronouns;
		this.traveler = traveler;
		this.sitMessage = sitMessage;
		this.unSitMessage = unSitMessage;
		this.deathMessage = deathMessage;
		this.killMessage = killMessage;
		this.saidMessage = saidMessage;
	}

	public @NotNull String getJoinMessage() {
		return this.joinMessage;
	}

	public @NotNull String getQuitMessage() {
		return this.quitMessage;
	}

	public @NotNull String getSpitMessage() {
		return this.spitMessage;
	}

	public @NotNull String getFartMessage() {
		return this.fartMessage;
	}

	public @NotNull String getPronouns() {
		return this.pronouns;
	}

	public @NotNull String getTraveler() {
		return this.traveler;
	}

	public @NotNull String getSitMessage() {
		return this.sitMessage;
	}

	public @NotNull String getUnSitMessage() {
		return this.unSitMessage;
	}

	public @NotNull String getDeathMessage() {
		return this.deathMessage;
	}

	public @NotNull String getKillMessage() {
		return this.killMessage;
	}

	public @NotNull String getSaidMessage() {
		return this.saidMessage;
	}
}
