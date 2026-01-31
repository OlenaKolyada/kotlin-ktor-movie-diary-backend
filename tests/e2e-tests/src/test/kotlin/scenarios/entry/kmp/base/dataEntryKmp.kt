package scenarios.entry.kmp.base

import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryCreateObject
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryDebug
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryRequestDebugMode
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryRequestDebugStubs

val debug = EntryDebug(mode = EntryRequestDebugMode.STUB, stub = EntryRequestDebugStubs.SUCCESS)

val someCreateEntry = EntryCreateObject(
    movieId = "movie:tt0111161",
    viewingDate = "2025-10-15",
    rating = 9,
    comment = "Потрясающий фильм о надежде"
)
