package me.buddha.chiplesspoker.data.model

data class Player(
    val name: String,
    val seatNumber: Int,
    val amount: Long,
    val isDealer: Boolean = false,
)
