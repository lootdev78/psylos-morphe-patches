# Psylos SoundCloud Patches

A SoundCloud-only patch set for use with Morphe.

## Compatibility

- App: SoundCloud (`com.soundcloud.android`)
- Verified target: `2026.08.26-release`

## Patches

### Private Psylos patches

- **Download button hook** — reproduces the workflow from the supplied Morphed SoundCloud APK. When enabled in the in-app Psylos settings, public `soundcloud.com` page links are routed with Android `ACTION_SEND` to a user-configured package. The patch does not include a downloader or media-decryption logic.
- **Hide cast icon** — adds a setting that hides SoundCloud's cast `MenuItem` and `ThemeableMediaRouteButton`.

The in-app settings entry contains only settings for the private patches that were actually selected during patching. No bottom-navigation, upload, inbox, notification, home-section, or other hide/navigation options from the supplied APK are included.

### Retained upstream SoundCloud patches

- **Disable telemetry** — original Hoo-dles patch, copyright header retained in source.
- **AMOLED dark theme** — original Hoo-dles patch, copyright header retained in source.

The subscription/paywall-unlock patch from the source archive is not included in this scoped build.

## Credits and licensing

See `CREDITS.md`, `LICENSE`, and `NOTICE`. The project name is intentionally distinct from Morphe because the included Section 7 notice permits Morphe only as a descriptive compatibility reference for derivative works.

## Build

Use the included Gradle wrapper in an environment with access to the configured Gradle distribution and Morphe package registry dependencies:

```sh
./gradlew build
```
