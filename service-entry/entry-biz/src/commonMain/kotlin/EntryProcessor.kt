package com.funkycorgi.vulpecula.entry.biz

import com.funkycorgi.vulpecula.entry.biz.general.initStatus
import com.funkycorgi.vulpecula.entry.biz.general.operation
import com.funkycorgi.vulpecula.entry.biz.general.stubs
import com.funkycorgi.vulpecula.entry.biz.repo.*
import com.funkycorgi.vulpecula.entry.biz.stubs.*
import com.funkycorgi.vulpecula.entry.biz.validation.*
import com.funkycorgi.vulpecula.entry.cor.chain
import com.funkycorgi.vulpecula.entry.cor.rootChain
import com.funkycorgi.vulpecula.entry.cor.worker
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.EntryCorSettings
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryLock
import com.funkycorgi.vulpecula.entry.common.models.MovieId

@Suppress("unused", "RedundantSuspendModifier")
class EntryProcessor(
    private val corSettings: EntryCorSettings = EntryCorSettings.NONE,
) {
    private val businessChain = rootChain<EntryContext> {
        initStatus("Initialize status")
        initRepo("Initialize repository")

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
            validation {
                worker("Copy fields to entryValidating") { entryValidating = entryRequest.deepCopy() }
                worker("Clear id") { entryValidating.id = EntryId.NONE }
                worker("Trim movieId") { entryValidating.movieId = MovieId(entryValidating.movieId.asString().trim()) }
                worker("Trim comment") { entryValidating.comment = entryValidating.comment.trim() }
                validateMovieIdNotEmpty("Check movieId is not empty")
                validateMovieIdProperFormat("Check movieId format")
                validateViewingDateNotEmpty("Check viewingDate is not empty")
                validateRatingInRange("Check rating range")
                validateCommentHasContent("Check comment content")
                finishEntryValidation("Finish entry validation")
            }
            chain {
                title = "Create repository logic"
                repoPrepareCreate("Prepare entry for create")
                repoCreate("Create entry in repository")
            }
            prepareResult("Prepare create response")
        }

        operation("Read entry", EntryCommand.READ) {
            stubs("Process stubs") {
                stubReadSuccess("Prepare successful read response")
                stubValidationBadId("Prepare bad id error")
                stubDbError("Prepare database error")
                stubNoCase("Prepare unsupported stub error")
            }
            validation {
                worker("Copy fields to entryValidating") { entryValidating = entryRequest.deepCopy() }
                worker("Trim id") { entryValidating.id = EntryId(entryValidating.id.asString().trim()) }
                validateIdNotEmpty("Check id is not empty")
                validateIdProperFormat("Check id format")
                finishEntryValidation("Finish entry validation")
            }
            chain {
                title = "Read repository logic"
                repoRead("Read entry from repository")
                worker {
                    title = "Prepare read response"
                    on { state == com.funkycorgi.vulpecula.entry.common.models.EntryState.RUNNING }
                    handle { entryRepoDone = entryRepoRead }
                }
            }
            prepareResult("Prepare read response")
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
            validation {
                worker("Copy fields to entryValidating") { entryValidating = entryRequest.deepCopy() }
                worker("Trim id") { entryValidating.id = EntryId(entryValidating.id.asString().trim()) }
                worker("Trim lock") { entryValidating.lock = EntryLock(entryValidating.lock.asString().trim()) }
                worker("Trim movieId") { entryValidating.movieId = MovieId(entryValidating.movieId.asString().trim()) }
                worker("Trim comment") { entryValidating.comment = entryValidating.comment.trim() }
                validateIdNotEmpty("Check id is not empty")
                validateIdProperFormat("Check id format")
                validateLockNotEmpty("Check lock is not empty")
                validateLockProperFormat("Check lock format")
                validateMovieIdNotEmpty("Check movieId is not empty")
                validateMovieIdProperFormat("Check movieId format")
                validateViewingDateNotEmpty("Check viewingDate is not empty")
                validateRatingInRange("Check rating range")
                validateCommentHasContent("Check comment content")
                finishEntryValidation("Finish entry validation")
            }
            chain {
                title = "Update repository logic"
                repoRead("Read entry before update")
                repoPrepareUpdate("Prepare entry for update")
                repoUpdate("Update entry in repository")
            }
            prepareResult("Prepare update response")
        }

        operation("Delete entry", EntryCommand.DELETE) {
            stubs("Process stubs") {
                stubDeleteSuccess("Prepare successful delete response")
                stubValidationBadId("Prepare bad id error")
                stubCannotDelete("Prepare cannot delete error")
                stubDbError("Prepare database error")
                stubNoCase("Prepare unsupported stub error")
            }
            validation {
                worker("Copy fields to entryValidating") { entryValidating = entryRequest.deepCopy() }
                worker("Trim id") { entryValidating.id = EntryId(entryValidating.id.asString().trim()) }
                worker("Trim lock") { entryValidating.lock = EntryLock(entryValidating.lock.asString().trim()) }
                validateIdNotEmpty("Check id is not empty")
                validateIdProperFormat("Check id format")
                validateLockNotEmpty("Check lock is not empty")
                validateLockProperFormat("Check lock format")
                finishEntryValidation("Finish entry validation")
            }
            chain {
                title = "Delete repository logic"
                repoRead("Read entry before delete")
                repoPrepareDelete("Prepare entry for delete")
                repoDelete("Delete entry from repository")
            }
            prepareResult("Prepare delete response")
        }

        operation("Search entries", EntryCommand.SEARCH) {
            stubs("Process stubs") {
                stubSearchSuccess("Prepare successful search response")
                stubValidationBadSearchString("Prepare bad searchString error")
                stubDbError("Prepare database error")
                stubNoCase("Prepare unsupported stub error")
            }
            validation {
                worker("Copy fields to entryFilterValidating") { entryFilterValidating = entryFilterRequest.deepCopy() }
                validateSearchStringLength("Check searchString length")
                finishEntryFilterValidation("Finish entry filter validation")
            }
            repoSearch("Search entries in repository")
            prepareResult("Prepare search response")
        }
    }.build()

    suspend fun exec(ctx: EntryContext) {
        businessChain.exec(ctx.also { it.corSettings = corSettings })
    }
}
