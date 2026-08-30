/**
 * Private patch by Psylos.
 * Reconstructed from the supplied Morphed SoundCloud APK.
 */
package psylos.morphe.extension.soundcloud;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.LruCache;
import android.view.View;
import android.widget.Toast;

import com.soundcloud.android.ui.components.buttons.DownloadActionButton;

public final class DownloadButtonHook {
    private static final LruCache<String, String> URL_CACHE = new LruCache<>(128);

    private DownloadButtonHook() {}

    public static boolean isEnabled() {
        return SoundCloudSettings.DOWNLOAD_HOOK_ENABLED.get().booleanValue();
    }

    public static void applyLibraryDownloadsVisibility(View view) {
        if (view != null && isEnabled()) view.setVisibility(View.GONE);
    }

    public static boolean filterNativeDownloadUiVisibility(boolean visible) {
        return isEnabled() ? false : visible;
    }

    public static DownloadActionButton.State normalizeDownloadState(DownloadActionButton.State state) {
        return isEnabled() ? DownloadActionButton.State.c : state;
    }

    public static void rememberUrl(Object key, String url) {
        if (key != null && isPublicSoundCloudUrl(url)) {
            URL_CACHE.put(String.valueOf(key), url);
        }
    }

    public static String getRememberedUrl(Object key) {
        if (key == null) return null;
        return URL_CACHE.get(String.valueOf(key));
    }

    public static boolean tryShareRemembered(Context context, Object key) {
        return tryShare(context, getRememberedUrl(key));
    }

    public static boolean tryShare(Context context, String url) {
        if (!isEnabled()) return false;
        if (context == null) return true;

        if (!isPublicSoundCloudUrl(url)) {
            toast(context, "SoundCloud-Link nicht verfuegbar");
            return true;
        }

        String targetPackage = SoundCloudSettings.DOWNLOAD_TARGET_PACKAGE.get();
        targetPackage = targetPackage == null ? "" : targetPackage.trim();
        if (targetPackage.isEmpty()) {
            toast(context, "Custom Package Name fehlt");
            return true;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.setPackage(targetPackage);
        if (!(context instanceof Activity)) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException | SecurityException | IllegalArgumentException ex) {
            toast(context, "Externe Download-App nicht gefunden oder ACTION_SEND nicht akzeptiert");
        }
        return true;
    }

    private static boolean isPublicSoundCloudUrl(String url) {
        if (url == null) return false;
        String trimmed = url.trim();
        if (trimmed.isEmpty()) return false;

        Uri parsed = Uri.parse(trimmed);
        String scheme = parsed.getScheme();
        if (scheme == null || !(scheme.equalsIgnoreCase("https") || scheme.equalsIgnoreCase("http"))) {
            return false;
        }

        String host = parsed.getHost();
        return host != null &&
                (host.equalsIgnoreCase("soundcloud.com") || host.equalsIgnoreCase("www.soundcloud.com"));
    }

    private static void toast(Context context, String text) {
        Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
