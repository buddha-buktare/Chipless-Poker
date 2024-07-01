package me.buddha.chiplesspoker.domain

import kotlinx.serialization.Serializable

class Destination {
    @Serializable
    object Home

    @Serializable
    object CreateTable
}