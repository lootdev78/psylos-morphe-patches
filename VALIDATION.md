# Validation

Target inspected from the supplied APK:

- Package: `com.soundcloud.android`
- Version: `2026.08.26-release`
- Version code: `369070`

Checks performed for this scoped source tree:

- Decompiled the supplied APK with the supplied apktool 3.0.3.
- Matched all 14 target class/method groups used by the private Download button hook, Hide cast icon, and settings integration against the supplied APK.
- Verified the reconstructed Download button hook workflow covers track and playlist URL caching, track/playlist bottom-sheet actions, details-page actions, download-state normalization, library Downloads-row visibility, and native Offline Listening settings visibility.
- Verified the Hide cast icon workflow covers both the `MenuItem` and view-backed cast button paths in `DefaultCastButtonInstaller`.
- Verified unwanted bottom-navigation, upload, inbox, notification, home-section, and other Morphed hide/navigation settings are absent from the new Psylos private patch/extension sources.
- Verified the only app compatibility declaration in the patch source is SoundCloud (`com.soundcloud.android`).
- Compiled the new Kotlin patch sources against local API/dexlib compile stubs successfully.
- Compiled the new Java extension sources against local Android/Morphe/SoundCloud compile stubs successfully.

## Full Gradle build limitation

A full `./gradlew build` could not be completed in this sandbox because the Gradle wrapper distribution host (`services.gradle.org`) could not be resolved from the network-isolated environment. This is an environment/dependency-fetch limitation, so the final repository has not been claimed as fully Gradle-built here.
