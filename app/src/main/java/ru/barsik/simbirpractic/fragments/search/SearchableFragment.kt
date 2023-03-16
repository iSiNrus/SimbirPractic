package ru.barsik.simbirpractic.fragments.search

import kotlinx.coroutines.flow.Flow

/**
 *
 * @author Mitryashkin
 * @since 02.03.2023
 */
interface SearchableFragment {
    suspend fun setSearchQuery(query: String)
}