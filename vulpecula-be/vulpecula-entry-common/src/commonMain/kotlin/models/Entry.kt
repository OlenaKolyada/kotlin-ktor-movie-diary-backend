package com.funkycorgi.vulpecula.entry.common.models

data class Entry(
    var id: EntryId = EntryId.NONE,
    var userId: UserId = UserId.NONE,
    var movieId: MovieId = MovieId.NONE,
    var viewingDate: ViewingDate = ViewingDate.NONE,
    var rating: Int = 0,
    var comment: String = "",
    var lock: EntryLock = EntryLock.NONE,
    val userPermissions: MutableSet<EntryUserPermission> = mutableSetOf(),
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Entry()
    }
}
