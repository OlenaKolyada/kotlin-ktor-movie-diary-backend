package com.funkycorgi.vulpecula.entry.biz.repo

import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.fail
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.repo.errorRepoConcurrency
import com.funkycorgi.vulpecula.entry.cor.ICorChainDsl
import com.funkycorgi.vulpecula.entry.cor.worker

fun ICorChainDsl<EntryContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если lock не совпадает с сохранённым в БД,
        данные запроса устарели и нужно обновить их вручную.
    """.trimIndent()
    on { state == EntryState.RUNNING && entryValidated.lock != entryRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(entryRepoRead, entryValidated.lock).errors)
    }
}
