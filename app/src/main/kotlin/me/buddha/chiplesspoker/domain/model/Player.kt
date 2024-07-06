package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.PlayerEntity
import me.buddha.chiplesspoker.domain.utils.PlayingStatus

data class Player(
    var name: String,
    val seatNumber: Int,
    val chips: Long = 1000,
    val isDealer: Boolean = false,
    val playingStatus: PlayingStatus,
)

fun Player.asEntity() = PlayerEntity (
    name = this.name,
    seatNumber = this.seatNumber,
    chips = this.chips,
    isDealer = this.isDealer,
    playingStatus = this.playingStatus,
)