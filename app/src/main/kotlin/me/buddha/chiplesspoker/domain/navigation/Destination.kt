package me.buddha.chiplesspoker.domain.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object Home : Destination

    @Serializable
    data object CreateTable : Destination

    @Serializable
    data class RunningTable(
        val id: Long,
    ) : Destination
}