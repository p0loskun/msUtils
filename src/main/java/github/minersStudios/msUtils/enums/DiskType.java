package github.minersStudios.msUtils.enums;

import javax.annotation.Nonnull;

public enum DiskType {
	DROPBOX, YANDEX_DISK;

	/**
	 * @param name Disk name
	 * @return DiskType by name
	 */
	@Nonnull
	public static DiskType getDiskTypeByString(@Nonnull String name){
		return name.equals("YANDEX_DISK") ? YANDEX_DISK : DROPBOX;
	}
}
