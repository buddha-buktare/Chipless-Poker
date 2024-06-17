package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.Player

data class PlayerEntity(
    val name: String,
    val seatNumber: Int,
    val chips: Long,
    val isDealer: Boolean = false,
)

fun PlayerEntity.asExternalModel() = Player (
    name = this.name,
    seatNumber = this.seatNumber,
    chips = this.chips,
    isDealer = this.isDealer
)
