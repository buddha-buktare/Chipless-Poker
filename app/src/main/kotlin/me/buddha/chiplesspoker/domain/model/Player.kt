package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.PlayerEntity

data class Player(
    val name: String,
    val seatNumber: Int,
    val chips: Long,
    val isDealer: Boolean = false,
)

fun Player.asEntity() = PlayerEntity (
    name = this.name,
    seatNumber = this.seatNumber,
    chips = this.chips,
    isDealer = this.isDealer
)