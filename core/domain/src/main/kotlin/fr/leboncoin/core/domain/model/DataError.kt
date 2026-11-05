package fr.leboncoin.core.domain.model

/** Reasons why remote data could not be synchronized, independent from the HTTP stack. */
enum class DataError {
    /** No connectivity, timeout, DNS failure... */
    Network,

    /** The server answered with an error or an unreadable payload. */
    Server,

    /** Anything else. */
    Unknown,
}

sealed interface RefreshResult {
    data object Success : RefreshResult
    data class Failure(val error: DataError) : RefreshResult
}
