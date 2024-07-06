package me.buddha.chiplesspoker.domain.navigation

import kotlinx.serialization.Serializable

class Destination {
    @Serializable
    object Home

    @Serializable
    object CreateTable

    @Serializable
    data class RunningTable(
        val id: Long,
    )
}