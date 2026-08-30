/**
 * Private patch by Psylos.
 * Settings integration reconstructed from the supplied Morphed SoundCloud APK.
 */
package psylos.morphe.patches.soundcloud.privatepatches.settings

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.extension.ExtensionHook
import app.morphe.patches.all.misc.extension.sharedExtensionPatch
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

private object RootActivityOnCreateFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/architecture/view/RootActivity;",
    name = "onCreate",
    returnType = "V",
    parameters = listOf("Landroid/os/Bundle;")
)

private object SettingsScreenFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/settings/main/SettingsScreenKt;",
    name = "a",
    returnType = "V",
    parameters = listOf(
        "Lcom/soundcloud/android/settings/main/SettingsState;",
        "Lkotlin/jvm/functions/Function0;",
        "Lkotlin/jvm/functions/Function1;",
        "Landroidx/compose/runtime/Composer;",
        "I"
    )
)

internal object DownloadPatchInstalledFingerprint : Fingerprint(
    definingClass = "Lpsylos/morphe/extension/soundcloud/SoundCloudSettings;",
    name = "isDownloadPatchInstalled",
    returnType = "Z",
    parameters = emptyList()
)

internal object CastPatchInstalledFingerprint : Fingerprint(
    definingClass = "Lpsylos/morphe/extension/soundcloud/SoundCloudSettings;",
    name = "isCastPatchInstalled",
    returnType = "Z",
    parameters = emptyList()
)

private val extensionPatch = sharedExtensionPatch(
    listOf("soundcloud"),
    ExtensionHook(RootActivityOnCreateFingerprint)
)

internal val soundCloudSettingsPatch = bytecodePatch {
    dependsOn(extensionPatch)

    execute {
        SettingsScreenFingerprint.method.apply {
            val columnScopeIndex = implementation!!.instructions.indexOfFirst { instruction ->
                instruction.opcode == Opcode.SGET_OBJECT &&
                    instruction.getReference<FieldReference>()?.definingClass ==
                    "Landroidx/compose/foundation/layout/ColumnScopeInstance;"
            }
            require(columnScopeIndex >= 0) { "Could not locate the SoundCloud settings ColumnScope." }

            addInstruction(
                columnScopeIndex + 1,
                "invoke-static {p3}, Lpsylos/morphe/extension/soundcloud/MorpheSettingsCompose;->render(Landroidx/compose/runtime/Composer;)V"
            )
        }
    }
}
