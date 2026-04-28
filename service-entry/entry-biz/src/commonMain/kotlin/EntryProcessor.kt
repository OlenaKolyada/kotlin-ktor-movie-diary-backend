package com.funkycorgi.vulpecula.entry.biz

import com.funkycorgi.vulpecula.entry.biz.general.initStatus
import com.funkycorgi.vulpecula.entry.biz.general.operation
import com.funkycorgi.vulpecula.entry.biz.general.stubs
import com.funkycorgi.vulpecula.entry.biz.stubs.*
import com.funkycorgi.vulpecula.entry.cor.rootChain
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand

@Suppress("unused", "RedundantSuspendModifier")
class EntryProcessor(
    private val corSettings: EntryCorSettings = EntryCorSettings.NONE,
) {
    private val businessChain = rootChain<EntryContext> {
        initStatus("Initialize status")

        operation("Create entry", EntryCommand.CREATE) {
            stubs("Process stubs") {
                stubCreateSuccess("Prepare successful create response")
                stubValidationBadMovieId("Prepare bad movieId error")
                stubValidationBadRating("Prepare bad rating error")
                stubValidationBadViewingDate("Prepare bad viewingDate error")
                stubValidationBadComment("Prepare bad comment error")
                stubDbError("Prepare database error")
                stubNoCase("Prepare unsupported stub error")
            }
        }

        operation("Read entry", EntryCommand.READ) {
            stubs("Process stubs") {
                stubReadSuccess("Prepare successful read response")
                stubValidationBadId("Prepare bad id error")
                stubDbError("Prepare database error")
                stubNoCase("Prepare unsupported stub error")
            }
        }

        operation("Update entry", EntryCommand.UPDATE) {
            stubs("Process stubs") {
                stubUpdateSuccess("Prepare successful update response")
                stubValidationBadId("Prepare bad id error")
                stubValidationBadMovieId("Prepare bad movieId error")
                stubValidationBadRating("Prepare bad rating error")
                stubValidationBadViewingDate("Prepare bad viewingDate error")
                stubValidationBadComment("Prepare bad comment error")
                stubDbError("Prepare database error")
                stubNoCase("Prepare unsupported stub error")
            }
        }

        operation("Delete entry", EntryCommand.DELETE) {
            stubs("Process stubs") {
                stubDeleteSuccess("Prepare successful delete response")
                stubValidationBadId("Prepare bad id error")
                stubCannotDelete("Prepare cannot delete error")
                stubDbError("Prepare database error")
                stubNoCase("Prepare unsupported stub error")
            }
        }

        operation("Search entries", EntryCommand.SEARCH) {
            stubs("Process stubs") {
                stubSearchSuccess("Prepare successful search response")
                stubValidationBadSearchString("Prepare bad searchString error")
                stubDbError("Prepare database error")
                stubNoCase("Prepare unsupported stub error")
            }
        }
    }.build()

    suspend fun exec(ctx: EntryContext) {
        businessChain.exec(ctx.also { it.corSettings = corSettings })
    }
}
