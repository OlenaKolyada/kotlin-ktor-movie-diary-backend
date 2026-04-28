package com.funkycorgi.vulpecula.entry.biz.validation

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.errorValidation
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.chain
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.validateSearchStringLength(title: String) = chain {
    this.title = title
    on { state == EntryState.RUNNING }

    worker("Trim search string") {
        entryFilterValidating.searchString = entryFilterValidating.searchString.trim()
    }

    worker {
        this.title = "Check search string minimum length"
        on { state == EntryState.RUNNING && entryFilterValidating.searchString.length in 1..2 }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooShort",
                    description = "search string must contain at least 3 symbols"
                )
            )
        }
    }

    worker {
        this.title = "Check search string maximum length"
        on { state == EntryState.RUNNING && entryFilterValidating.searchString.length > 100 }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooLong",
                    description = "search string must be no more than 100 symbols long"
                )
            )
        }
    }
}
