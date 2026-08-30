/**
 * Private patch by Psylos.
 * Download-button workflow reconstructed from the supplied Morphed SoundCloud APK.
 */
package psylos.morphe.patches.soundcloud.privatepatches.download

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.getReference
import app.morphe.util.returnEarly
import psylos.morphe.patches.soundcloud.privatepatches.settings.DownloadPatchInstalledFingerprint
import psylos.morphe.patches.soundcloud.privatepatches.settings.soundCloudSettingsPatch
import psylos.morphe.patches.soundcloud.shared.Constants
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.TypeReference

private const val HOOK = "Lpsylos/morphe/extension/soundcloud/DownloadButtonHook;"

@Suppress("unused")
val downloadButtonHookPatch = bytecodePatch(
    name = "Download button hook",
    description = "Routes SoundCloud download-button actions to a user-selected external app via ACTION_SEND.",
    default = false
) {
    compatibleWith(Constants.COMPATIBILITY)
    dependsOn(soundCloudSettingsPatch)

    execute {
        // Expose only this section in the shared Psylos settings UI.
        DownloadPatchInstalledFingerprint.method.returnEarly(true)

        // Track menu: when enabled, a downloaded item is presented as a download action.
        TrackRemoveMenuCtorFingerprint.method.addInstructions(2, """
            invoke-static {}, $HOOK->isEnabled()Z
            move-result v2
            if-eqz v2, :psylos_track_menu_continue
            sget v0, Lcom/soundcloud/android/features/bottomsheet/track/R${'$'}string;->menu_add_to_downloads:I
            sget v1, Lcom/soundcloud/android/ui/components/R${'$'}drawable;->ic_actions_download_initial:I
            :psylos_track_menu_continue
        """.trimIndent())

        // Playlist menu: same behavior for an already-downloaded playlist entry.
        PlaylistDownloadedMenuCtorFingerprint.method.addInstructions(2, """
            invoke-static {}, $HOOK->isEnabled()Z
            move-result v2
            if-eqz v2, :psylos_playlist_menu_continue
            sget v1, Lcom/soundcloud/android/features/bottomsheet/playlist/R${'$'}string;->download_playlist:I
            sget v4, Lcom/soundcloud/android/ui/components/R${'$'}drawable;->ic_actions_download_initial:I
            :psylos_playlist_menu_continue
        """.trimIndent())

        // Remember playlist permalink against the playlist URN.
        PlaylistLoaderFingerprint.method.apply {
            val permalinkCall = implementation!!.instructions.indexOfFirst { instruction ->
                instruction.getReference<MethodReference>()?.name == "getPermalinkUrl"
            }
            require(permalinkCall >= 0) { "Playlist permalink call not found." }
            addInstruction(permalinkCall + 2, "invoke-static {v6, v7}, $HOOK->rememberUrl(Ljava/lang/Object;Ljava/lang/String;)V")
        }

        // Intercept both playlist Download and Downloaded actions.
        PlaylistBottomSheetClickFingerprint.method.apply {
            val targets = listOf(
                "Lcom/soundcloud/android/features/bottomsheet/playlist/PlaylistMenuItem\$Download;" to "psylos_playlist_download_continue",
                "Lcom/soundcloud/android/features/bottomsheet/playlist/PlaylistMenuItem\$Downloaded;" to "psylos_playlist_downloaded_continue"
            )
            for ((type, label) in targets.reversed()) {
                val instructions = implementation!!.instructions
                val branchIndex = instructions.indexOfFirst { instruction ->
                    instruction.opcode == Opcode.INSTANCE_OF && instruction.getReference<TypeReference>()?.type == type
                }
                require(branchIndex >= 0) { "Playlist branch $type not found." }
                val requireContextIndex = instructions.withIndex().firstOrNull { (index, instruction) ->
                    index > branchIndex && instruction.getReference<MethodReference>()?.name == "requireContext"
                }?.index ?: error("requireContext after $type not found.")
                addInstructions(requireContextIndex + 2, """
                    invoke-static {v4, v1}, $HOOK->tryShareRemembered(Landroid/content/Context;Ljava/lang/Object;)Z
                    move-result v4
                    if-eqz v4, :$label
                    invoke-virtual {v2}, Landroidx/fragment/app/DialogFragment;->dismissAllowingStateLoss()V
                    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;
                    return-object v0
                    :$label
                """.trimIndent())
            }
        }

        // Track bottom sheet: intercept RemoveFromDownload and SelectiveDownload using its stored permalink.
        TrackBottomSheetClickFingerprint.method.apply {
            val targets = listOf(
                "Lcom/soundcloud/android/features/bottomsheet/track/TrackMenuItem\$RemoveFromDownload;" to Pair("v1", "psylos_track_remove_continue"),
                "Lcom/soundcloud/android/features/bottomsheet/track/TrackMenuItem\$SelectiveDownload;" to Pair("v3", "psylos_track_selective_continue")
            )
            for ((type, config) in targets.reversed()) {
                val (contextReg, label) = config
                val instructions = implementation!!.instructions
                val branchIndex = instructions.indexOfFirst { instruction ->
                    instruction.opcode == Opcode.INSTANCE_OF && instruction.getReference<TypeReference>()?.type == type
                }
                require(branchIndex >= 0) { "Track branch $type not found." }
                val requireContextIndex = instructions.withIndex().firstOrNull { (index, instruction) ->
                    index > branchIndex && instruction.getReference<MethodReference>()?.name == "requireContext"
                }?.index ?: error("requireContext after $type not found.")
                addInstructions(requireContextIndex + 2, """
                    invoke-static {$contextReg, v2}, $HOOK->tryShare(Landroid/content/Context;Ljava/lang/String;)Z
                    move-result $contextReg
                    if-eqz $contextReg, :$label
                    invoke-virtual {v8}, Landroidx/fragment/app/DialogFragment;->dismissAllowingStateLoss()V
                    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;
                    return-object v0
                    :$label
                """.trimIndent())
            }
        }

        // Remember track permalink against its TrackUrn.
        TrackFetchFingerprint.method.apply {
            val permalinkCall = implementation!!.instructions.indexOfFirst { instruction ->
                instruction.getReference<MethodReference>()?.name == "getPermalinkUrl"
            }
            require(permalinkCall >= 0) { "Track permalink call not found." }
            addInstruction(permalinkCall + 2, "invoke-static {v1, p0}, $HOOK->rememberUrl(Ljava/lang/Object;Ljava/lang/String;)V")
        }

        // Track-page download click: send the remembered link and stop the native offline action when handled.
        TrackDownloadClickFingerprint.method.apply {
            val requireContextIndex = implementation!!.instructions.indexOfFirst { instruction ->
                instruction.getReference<MethodReference>()?.name == "requireContext"
            }
            require(requireContextIndex >= 0) { "Track-page requireContext call not found." }
            addInstructions(requireContextIndex + 2, """
                invoke-static {v2, v1}, $HOOK->tryShareRemembered(Landroid/content/Context;Ljava/lang/Object;)Z
                move-result v2
                if-eqz v2, :psylos_track_page_click_continue
                return-void
                :psylos_track_page_click_continue
            """.trimIndent())
        }

        // Force the native action-button state to the normal download state while the hook is enabled.
        TrackPageDownloadStateFingerprint.method.apply {
            val viewStateCtor = implementation!!.instructions.indexOfFirst { instruction ->
                instruction.opcode == Opcode.NEW_INSTANCE &&
                    instruction.getReference<TypeReference>()?.type ==
                    "Lcom/soundcloud/android/ui/components/buttons/DownloadActionButton\$ViewState;"
            }
            require(viewStateCtor >= 0) { "Track download ViewState constructor not found." }
            addInstructions(viewStateCtor, """
                invoke-static {v0}, $HOOK->normalizeDownloadState(Lcom/soundcloud/android/ui/components/buttons/DownloadActionButton${'$'}State;)Lcom/soundcloud/android/ui/components/buttons/DownloadActionButton${'$'}State;
                move-result-object v0
            """.trimIndent())
        }

        PlaylistRendererFingerprint.method.apply {
            val viewStateCtor = implementation!!.instructions.indexOfFirst { instruction ->
                instruction.opcode == Opcode.NEW_INSTANCE &&
                    instruction.getReference<TypeReference>()?.type ==
                    "Lcom/soundcloud/android/ui/components/buttons/DownloadActionButton\$ViewState;"
            }
            require(viewStateCtor >= 0) { "Playlist download ViewState constructor not found." }
            addInstructions(viewStateCtor, """
                invoke-static {v8}, $HOOK->normalizeDownloadState(Lcom/soundcloud/android/ui/components/buttons/DownloadActionButton${'$'}State;)Lcom/soundcloud/android/ui/components/buttons/DownloadActionButton${'$'}State;
                move-result-object v8
            """.trimIndent())
        }

        // Playlist details page: preserve the clicked View before p1 is reused, then send its public permalink.
        PlaylistDetailsClickFingerprint.method.apply {
            addInstruction(0, "move-object v1, p1")
            val permalinkCall = implementation!!.instructions.indexOfFirst { instruction ->
                instruction.getReference<MethodReference>()?.name == "getPermalinkUrl"
            }
            require(permalinkCall >= 0) { "Playlist details permalink call not found." }
            addInstructions(permalinkCall + 2, """
                invoke-virtual {v1}, Landroid/view/View;->getContext()Landroid/content/Context;
                move-result-object v1
                invoke-static {v1, p1}, $HOOK->tryShare(Landroid/content/Context;Ljava/lang/String;)Z
                move-result v1
                if-eqz v1, :psylos_playlist_details_continue
                return-void
                :psylos_playlist_details_continue
            """.trimIndent())
        }

        // Hide SoundCloud's native Downloads library row while this alternate workflow is active.
        LibraryLinksFingerprint.method.apply {
            val downloadsFieldIndex = implementation!!.instructions.indexOfFirst { instruction ->
                val ref = instruction.getReference<FieldReference>()
                ref?.definingClass == "Lcom/soundcloud/android/features/library/LibraryLinksViewHolder;" &&
                    ref.name == "a" &&
                    ref.type == "Lcom/soundcloud/android/ui/components/actionlists/ActionListItem;"
            }
            require(downloadsFieldIndex >= 0) { "Library downloads row field not found." }
            addInstruction(downloadsFieldIndex + 1, "invoke-static {v3}, $HOOK->applyLibraryDownloadsVisibility(Landroid/view/View;)V")
        }

        // Hide native Offline Listening settings when the alternate workflow is enabled.
        SettingsScreenFingerprint.method.apply {
            val offlineVisibleIndex = implementation!!.instructions.indexOfFirst { instruction ->
                instruction.opcode == Opcode.INSTANCE_OF &&
                    instruction.getReference<TypeReference>()?.type ==
                    "Lcom/soundcloud/android/settings/main/OfflineSyncSettingState\$Visible;"
            }
            require(offlineVisibleIndex >= 0) { "OfflineSyncSettingState.Visible check not found." }
            addInstructions(offlineVisibleIndex + 1, """
                invoke-static {v6}, $HOOK->filterNativeDownloadUiVisibility(Z)Z
                move-result v6
            """.trimIndent())
        }
    }
}
