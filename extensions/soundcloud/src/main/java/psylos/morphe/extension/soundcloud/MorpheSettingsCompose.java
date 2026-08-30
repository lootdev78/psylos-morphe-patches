/** Private settings entry by Psylos. */
package psylos.morphe.extension.soundcloud;

import androidx.compose.runtime.Composer;

import com.soundcloud.android.ui.components.compose.actionlists.ActionListItemKt;

import app.morphe.extension.shared.ResourceUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public final class MorpheSettingsCompose {
    private MorpheSettingsCompose() {}

    public static void render(Composer composer) {
        Integer chevron = Integer.valueOf(ResourceUtils.getDrawableIdentifier("ic_actions_chevron_right"));
        ActionListItemKt.a(
                "Psylos SoundCloud Patches",
                new OpenClick(),
                null,
                false,
                false,
                null,
                chevron,
                null,
                null,
                composer,
                0,
                0x1bc
        );
    }

    private static final class OpenClick implements Function0<Unit> {
        @Override
        public Unit invoke() {
            MorpheSettingsUi.show();
            return Unit.INSTANCE;
        }
    }
}
