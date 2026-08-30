/** Private settings UI by Psylos. */
package psylos.morphe.extension.soundcloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import app.morphe.extension.shared.Utils;

public final class MorpheSettingsUi {
    private MorpheSettingsUi() {}

    public static void show() {
        Activity activity = Utils.getActivity();
        if (activity == null || activity.isFinishing()) return;

        int pad = dp(activity, 20);
        int small = dp(activity, 8);

        LinearLayout content = new LinearLayout(activity);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(pad, small, pad, small);

        final Switch downloadEnabled;
        final EditText packageInput;
        if (SoundCloudSettings.isDownloadPatchInstalled()) {
            TextView downloadHeader = header(activity, "Download-Button-Hook");
            content.addView(downloadHeader);

            downloadEnabled = new Switch(activity);
            downloadEnabled.setText("Download-Button-Hook aktivieren");
            downloadEnabled.setChecked(SoundCloudSettings.DOWNLOAD_HOOK_ENABLED.get().booleanValue());
            content.addView(downloadEnabled, matchWrap());

            packageInput = new EditText(activity);
            packageInput.setHint("Custom Package Name, z. B. com.example.downloader");
            packageInput.setSingleLine(true);
            packageInput.setInputType(InputType.TYPE_CLASS_TEXT);
            String currentPackage = SoundCloudSettings.DOWNLOAD_TARGET_PACKAGE.get();
            packageInput.setText(currentPackage == null ? "" : currentPackage);
            content.addView(packageInput, matchWrap());

            TextView downloadInfo = body(activity,
                    "Der SoundCloud-Link wird per Android ACTION_SEND an dieses Paket uebergeben.");
            content.addView(downloadInfo, matchWrap());
        } else {
            downloadEnabled = null;
            packageInput = null;
        }

        final Switch hideCast;
        if (SoundCloudSettings.isCastPatchInstalled()) {
            TextView castHeader = header(activity, "Hide Cast Icon");
            castHeader.setPadding(0, pad, 0, 0);
            content.addView(castHeader);

            hideCast = new Switch(activity);
            hideCast.setText("Cast-Icon ausblenden");
            hideCast.setChecked(SoundCloudSettings.HIDE_CAST_ICON.get().booleanValue());
            content.addView(hideCast, matchWrap());
        } else {
            hideCast = null;
        }

        ScrollView scroll = new ScrollView(activity);
        scroll.addView(content);

        new AlertDialog.Builder(activity)
                .setTitle("Psylos Morphe Patches")
                .setView(scroll)
                .setPositiveButton("Speichern", (dialog, which) -> {
                    if (downloadEnabled != null && packageInput != null) {
                        SoundCloudSettings.DOWNLOAD_HOOK_ENABLED.save(Boolean.valueOf(downloadEnabled.isChecked()));
                        SoundCloudSettings.DOWNLOAD_TARGET_PACKAGE.save(packageInput.getText().toString().trim());
                    }
                    if (hideCast != null) {
                        SoundCloudSettings.HIDE_CAST_ICON.save(Boolean.valueOf(hideCast.isChecked()));
                    }
                })
                .setNegativeButton("Abbrechen", null)
                .show();
    }

    private static TextView header(Context context, String text) {
        TextView view = new TextView(context);
        view.setText(text);
        view.setTextSize(18f);
        view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return view;
    }

    private static TextView body(Context context, String text) {
        TextView view = new TextView(context);
        view.setText(text);
        view.setTextSize(13f);
        return view;
    }

    private static ViewGroup.LayoutParams matchWrap() {
        return new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    private static int dp(Context context, int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
