package com.github.minersstudios.msutils.player;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSettings {
	private final @NotNull PlayerFile playerFile;
	private final @NotNull YamlConfiguration config;

	private final @NotNull Parameter<ResourcePack.Type> resourcePackType;

	public PlayerSettings(
			@NotNull PlayerFile playerFile
	) {
		this.playerFile = playerFile;
		this.config = playerFile.getYamlConfiguration();

		this.resourcePackType = new Parameter<>(
				"resource-pack.resource-pack-type",
				ResourcePack.Type.getResourcePackByString(this.config.getString("resource-pack.resource-pack-type", "NULL"))
		);
	}

	public void save() {
		this.playerFile.save();
	}

	public @NotNull Parameter<ResourcePack.Type> getResourcePackParam() {
		return this.resourcePackType;
	}

	public @Nullable ResourcePack.Type getResourcePackType() {
		return this.resourcePackType.getValue();
	}

	public void setResourcePackType(@Nullable ResourcePack.Type resourcePackType) {
		this.resourcePackType.setValue(resourcePackType);
		this.resourcePackType.setForYaml(this.config, resourcePackType == null ? null : resourcePackType.name());
	}

	public static class Parameter<V> {
		protected final @NotNull String path;
		protected V value;

		public Parameter(
				@NotNull String path,
				V value
		) {
			this.path = path;
			this.value = value;
		}

		public void setForYaml(@NotNull YamlConfiguration yamlConfiguration) {
			this.setForYaml(yamlConfiguration, this.value);
		}

		public void setForYaml(
				@NotNull YamlConfiguration yamlConfiguration,
				Object value
		) {
			yamlConfiguration.set(this.path, value);
		}

		public void saveForFile(@NotNull PlayerFile playerFile) {
			this.setForYaml(playerFile.getYamlConfiguration());
			playerFile.save();
		}

		public void saveForFile(
				@NotNull PlayerFile playerFile,
				Object value
		) {
			this.setForYaml(
					playerFile.getYamlConfiguration(),
					value
			);
			playerFile.save();
		}

		public @NotNull String getPath() {
			return this.path;
		}

		public V getValue() {
			return this.value;
		}

		public void setValue(V value) {
			this.value = value;
		}
	}
}
