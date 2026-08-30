# Psylos Morphe Patches

SoundCloud-only patches for use with Morphe.

## About

This repository is intentionally restricted to `com.soundcloud.android`.

Private patches by Psylos:
- Download button hook
- Hide cast icon

SoundCloud patches retained from `hoo-dles/morphe-patches` keep their original source credits in the corresponding source files.

## How to use these patches

After you create the GitHub repository, replace `YOUR_GITHUB_USERNAME` where needed and add the repository as a Morphe patch source:

`https://github.com/YOUR_GITHUB_USERNAME/psylos-morphe-patches`

Morphe deeplink:

`https://morphe.software/add-source?github=YOUR_GITHUB_USERNAME/psylos-morphe-patches`

## Patches list

<!-- PATCHES_START EXPANDED -->

#### The patch list is generated automatically by the release workflow.

<!-- PATCHES_END -->

## Development / releases

- Work on the `dev` branch.
- Build locally with `./gradlew buildAndroid`.
- The bundle is written to `patches/build/libs/patches-*.mpp`.
- Use semantic commits such as `feat:`, `fix:` and `chore:`.
- Merge `dev` into `main` without squashing for a stable release.
- Do not manually create GitHub releases; `.github/workflows/release.yml` and semantic-release update the generated release metadata.
- `patches-list.json`, `patches-bundle.json` and `CHANGELOG.md` are release-generated files.

## GitHub setup

Enable **Settings > Actions > General > Workflow permissions > Allow GitHub Actions to create and approve pull requests** so the `dev` -> `main` workflow can create its pull request.

## Credits

See [CREDITS.md](CREDITS.md). Existing upstream copyright notices are preserved in source files.

## License

GPLv3. See [LICENSE](LICENSE) and [NOTICE](NOTICE).
