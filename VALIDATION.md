# Validation

## Repository / Morphe source structure

Validated locally:

- `.github/workflows/build_pull_request.yml` present and YAML-valid.
- `.github/workflows/open_pull_request.yml` present and YAML-valid.
- `.github/workflows/release.yml` present and YAML-valid.
- `.github/dependabot.yml` present and YAML-valid.
- `patches-list.json` present and valid JSON, initialized as the Morphe template placeholder (`0.0.0`, empty list) until semantic release generates the first release metadata.
- `patches-bundle.json` present and valid JSON with the standard initial empty fields.
- `gradle.properties` uses the semantic-release baseline `version = 1.0.0`.
- project identity is `Psylos Morphe Patches` / `psylos-morphe-patches`.
- the release metadata source resolves from GitHub Actions' `GITHUB_REPOSITORY`; the local fallback is `YOUR_GITHUB_USERNAME/psylos-morphe-patches`.
- only `com.soundcloud.android` is declared as an app compatibility target.
- only SoundCloud patch source namespaces are present under `patches/src/main/kotlin/psylos/morphe/patches/soundcloud`.
- no unrelated app-request discussion template is included.

## Build validation

A real `./gradlew :patches:buildAndroid` was attempted. The build could not start in this isolated environment because Gradle Wrapper needs to fetch `gradle-9.7.1-bin.zip` from `services.gradle.org`, and DNS/network access to that host is unavailable here.

GitHub Actions or Termux with network access can perform the real Gradle build using the included workflows.
