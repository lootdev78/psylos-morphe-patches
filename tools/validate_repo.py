#!/usr/bin/env python3
from __future__ import annotations

import json
import os
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
errors: list[str] = []

required = [
    ".releaserc",
    "package.json",
    "package-lock.json",
    "patches-bundle.json",
    "patches-list.json",
    "gradlew",
    ".github/workflows/release.yml",
    ".github/workflows/open_pull_request.yml",
    ".github/workflows/build_pull_request.yml",
    "patches/build.gradle.kts",
    "settings.gradle.kts",
]
for rel in required:
    if not (ROOT / rel).is_file():
        errors.append(f"Missing required file: {rel}")

# gradlew must remain executable in git/Unix checkouts.
gradlew = ROOT / "gradlew"
if gradlew.exists() and os.name != "nt" and not os.access(gradlew, os.X_OK):
    errors.append("gradlew is not executable")

# Release metadata schema must always be parseable, even before first release.
try:
    bundle = json.loads((ROOT / "patches-bundle.json").read_text(encoding="utf-8"))
    expected = {"created_at", "description", "download_url", "signature_download_url", "version"}
    if set(bundle) != expected:
        errors.append(f"patches-bundle.json keys differ from Morphe schema: {sorted(bundle)}")
except Exception as exc:
    errors.append(f"Invalid patches-bundle.json: {exc}")

try:
    listing = json.loads((ROOT / "patches-list.json").read_text(encoding="utf-8"))
    if not isinstance(listing.get("patches"), list):
        errors.append("patches-list.json must contain a patches array")
    for patch in listing.get("patches", []):
        for compat in patch.get("compatiblePackages") or []:
            if compat.get("packageName") != "com.soundcloud.android":
                errors.append(
                    "Non-SoundCloud package in patches-list.json: "
                    + str(compat.get("packageName"))
                )
except Exception as exc:
    errors.append(f"Invalid patches-list.json: {exc}")

# The source tree itself is intentionally SoundCloud-only.
source_root = ROOT / "patches/src/main/kotlin"
packages = set()
if source_root.exists():
    rx = re.compile(r'packageName\s*=\s*"([^"]+)"')
    for path in source_root.rglob("*.kt"):
        text = path.read_text(encoding="utf-8", errors="replace")
        packages.update(rx.findall(text))
if packages != {"com.soundcloud.android"}:
    errors.append(f"Expected only com.soundcloud.android compatibility, got: {sorted(packages)}")

build = (ROOT / "patches/build.gradle.kts").read_text(encoding="utf-8", errors="replace")
for needle in [
    'lootdev78/psylos-morphe-patches',
    'name = "Psylos Morphe Patches"',
    'implementation(libs.gson)',
    'mainClass.set("app.morphe.util.PatchListGeneratorKt")',
    'dependsOn("generatePatchesList")',
]:
    if needle not in build:
        errors.append(f"Missing patches/build.gradle.kts requirement: {needle}")

release = (ROOT / ".github/workflows/release.yml").read_text(encoding="utf-8", errors="replace")
for needle in [
    "chmod +x gradlew",
    "cycjimmy/semantic-release-action@v6",
    "fetch-depth: 0",
]:
    if needle not in release:
        errors.append(f"Release workflow missing: {needle}")

releaserc = (ROOT / ".releaserc").read_text(encoding="utf-8", errors="replace")
if "./gradlew codegen" in releaserc:
    errors.append(".releaserc still calls removed codegen task")
if "generatePatchesList" not in releaserc:
    errors.append(".releaserc does not generate patches-list.json")

if errors:
    print("Repository validation failed:", file=sys.stderr)
    for error in errors:
        print(f"- {error}", file=sys.stderr)
    raise SystemExit(1)

print("Repository structure is valid.")
print("Compatibility scope: com.soundcloud.android only.")
print("Semantic Release metadata pipeline is present.")
