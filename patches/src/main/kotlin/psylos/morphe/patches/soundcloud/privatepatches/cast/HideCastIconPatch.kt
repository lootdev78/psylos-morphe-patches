/**
 * Private patch by Psylos.
 * Hide-cast workflow reconstructed from the supplied Morphed SoundCloud APK.
 */
package psylos.morphe.patches.soundcloud.privatepatches.cast

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.getReference
import app.morphe.util.returnEarly
import psylos.morphe.patches.soundcloud.privatepatches.settings.CastPatchInstalledFingerprint
import psylos.morphe.patches.soundcloud.privatepatches.settings.soundCloudSettingsPatch
import psylos.morphe.patches.soundcloud.shared.Constants
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

private const val CAST_HOOK = "Lpsylos/morphe/extension/soundcloud/CastIconHook;"

@Suppress("unused")
val hideCastIconPatch = bytecodePatch(
    name = "Hide cast icon",
    description = "Adds a Psylos SoundCloud setting that hides SoundCloud's cast icon.",
    default = false
) {
    compatibleWith(Constants.COMPATIBILITY)
    dependsOn(soundCloudSettingsPatch)

    execute {
        CastPatchInstalledFingerprint.method.returnEarly(true)

        // MenuItem-backed cast button: override its visibility before returning it.
        CastMenuInstallerFingerprint.method.apply {
            val optionalWrapIndex = implementation!!.instructions.indexOfFirst { instruction ->
                val ref = instruction.getReference<MethodReference>()
                ref?.definingClass == "Lcom/soundcloud/java/optional/Optional;" && ref.name == "g"
            }
            require(optionalWrapIndex >= 0) { "Cast Optional.g call not found." }
            addInstructions(optionalWrapIndex, """
                invoke-static {}, $CAST_HOOK->shouldShowCast()Z
                move-result p2
                invoke-interface {p1, p2}, Landroid/view/MenuItem;->setVisible(Z)Landroid/view/MenuItem;
            """.trimIndent())
        }

        // View-backed cast button: hide after SoundCloud installs/configures it.
        CastViewInstallerFingerprint.method.apply {
            val setFactoryIndex = implementation!!.instructions.indexOfFirst { instruction ->
                instruction.getReference<MethodReference>()?.name == "setDialogFactory"
            }
            require(setFactoryIndex >= 0) { "Cast setDialogFactory call not found." }
            addInstructions(setFactoryIndex + 1, """
                invoke-static {p1}, $CAST_HOOK->applyCastButtonVisibility(Landroid/view/View;)V
            """.trimIndent())
        }
    }
}
