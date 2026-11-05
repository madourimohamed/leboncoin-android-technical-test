package fr.leboncoin.core.testing

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData

/**
 * A [PagingData] holding [items] and declaring the pagination as complete.
 *
 * `PagingData.from(list)` alone reports more pages to come: consumers such as `asSnapshot`
 * or `LazyPagingItems` would then wait forever for them.
 */
fun <T : Any> fullyLoadedPagingData(items: List<T>): PagingData<T> {
    val complete = LoadState.NotLoading(endOfPaginationReached = true)
    return PagingData.from(
        data = items,
        sourceLoadStates = LoadStates(refresh = complete, prepend = complete, append = complete),
    )
}
