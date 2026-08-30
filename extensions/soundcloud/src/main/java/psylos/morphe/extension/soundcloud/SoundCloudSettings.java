/**
 * Private SoundCloud patches for Psylos Morphe Patches.
 */
package psylos.morphe.extension.soundcloud;

import app.morphe.extension.shared.settings.BooleanSetting;
import app.morphe.extension.shared.settings.StringSetting;

public final class SoundCloudSettings {
    public static final BooleanSetting DOWNLOAD_HOOK_ENABLED =
            new BooleanSetting("psylos_download_hook_enabled", Boolean.FALSE);
    public static final StringSetting DOWNLOAD_TARGET_PACKAGE =
            new StringSetting("psylos_download_hook_package", "");
    public static final BooleanSetting HIDE_CAST_ICON =
            new BooleanSetting("psylos_hide_cast_icon", Boolean.FALSE);

    public static boolean isDownloadPatchInstalled() {
        return false;
    }

    public static boolean isCastPatchInstalled() {
        return false;
    }

    private SoundCloudSettings() {}
}
