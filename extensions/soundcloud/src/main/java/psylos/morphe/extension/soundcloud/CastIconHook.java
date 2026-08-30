/** Private patch by Psylos. */
package psylos.morphe.extension.soundcloud;

import android.view.View;

public final class CastIconHook {
    private CastIconHook() {}

    public static boolean shouldShowCast() {
        return !SoundCloudSettings.HIDE_CAST_ICON.get().booleanValue();
    }

    public static void applyCastButtonVisibility(View view) {
        if (view != null && SoundCloudSettings.HIDE_CAST_ICON.get().booleanValue()) {
            view.setVisibility(View.GONE);
        }
    }
}
