/** Private patch by Psylos. */
package psylos.morphe.patches.soundcloud.privatepatches.download

import app.morphe.patcher.Fingerprint

internal object TrackRemoveMenuCtorFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/features/bottomsheet/track/TrackMenuItem\$RemoveFromDownload;",
    name = "<init>",
    returnType = "V",
    parameters = listOf("Lcom/soundcloud/android/foundation/domain/TrackUrn;")
)

internal object PlaylistDownloadedMenuCtorFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/features/bottomsheet/playlist/PlaylistMenuItem\$Downloaded;",
    name = "<init>",
    returnType = "V",
    parameters = listOf("Lcom/soundcloud/android/foundation/actions/models/DownloadParams\$Remove;")
)

internal object PlaylistLoaderFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/features/bottomsheet/playlist/PlaylistBottomSheetViewModel\$playlistLoader\$1\$3;",
    name = "a",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;", "Ljava/lang/Object;", "Ljava/lang/Object;")
)

internal object PlaylistBottomSheetClickFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/features/bottomsheet/playlist/c;",
    name = "invoke",
    returnType = "Ljava/lang/Object;",
    parameters = emptyList()
)

internal object TrackBottomSheetClickFingerprint : Fingerprint(
    definingClass = "La50/b;",
    name = "invoke",
    returnType = "Ljava/lang/Object;",
    parameters = emptyList()
)

internal object TrackFetchFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/trackpage/TrackDetailsPagePresenter\$fetchData\$2;",
    name = "apply",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;", "Ljava/lang/Object;")
)

internal object TrackDownloadClickFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/trackpage/TrackDetailsPagePresenter\$subscribeForDownloadClick\$1;",
    name = "accept",
    returnType = "V",
    parameters = listOf("Ljava/lang/Object;")
)

internal object TrackPageDownloadStateFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/trackpage/TrackDetailsPagePresenter\$toPageResultObservable\$1\$7\$1;",
    name = "invokeSuspend",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;")
)

internal object PlaylistRendererFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/playlist/view/renderers/PlaylistEngagementsPlayableRenderer;",
    name = "a",
    returnType = "V",
    parameters = listOf(
        "Landroid/view/View;",
        "Lcom/soundcloud/android/playlist/view/PlaylistDetailsInputs;",
        "Lcom/soundcloud/android/playlists/PlaylistDetailsMetadata;"
    )
)

internal object PlaylistDetailsClickFingerprint : Fingerprint(
    definingClass = "Lov/e;",
    name = "onClick",
    returnType = "V",
    parameters = listOf("Landroid/view/View;")
)

internal object LibraryLinksFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/features/library/LibraryLinksViewHolder;",
    name = "bindItem",
    returnType = "V",
    parameters = listOf("Ljava/lang/Object;")
)

internal object SettingsScreenFingerprint : Fingerprint(
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
