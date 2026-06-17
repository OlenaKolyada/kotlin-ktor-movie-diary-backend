package com.funkycorgi.vulpecula.entry.common.repo.exceptions

open class RepoException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
