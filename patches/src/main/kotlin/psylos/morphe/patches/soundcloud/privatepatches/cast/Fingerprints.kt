/** Private patch by Psylos. */
package psylos.morphe.patches.soundcloud.privatepatches.cast

import app.morphe.patcher.Fingerprint

internal object CastMenuInstallerFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/cast/ui/DefaultCastButtonInstaller;",
    name = "a",
    returnType = "Lcom/soundcloud/java/optional/Optional;",
    parameters = listOf("Landroidx/fragment/app/FragmentActivity;", "Landroid/view/Menu;", "I")
)

internal object CastViewInstallerFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/cast/ui/DefaultCastButtonInstaller;",
    name = "b",
    returnType = "V",
    parameters = listOf("Lcom/soundcloud/android/cast/ui/ThemeableMediaRouteButton;")
)
