package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.Player
import me.buddha.chiplesspoker.domain.utils.PlayingStatus

data class PlayerEntity(
    val name: String,
    val seatNumber: Int,
    val chips: Long,
    val isDealer: Boolean = false,
    val playingStatus: PlayingStatus,
)

fun PlayerEntity.asExternalModel() = Player (
    name = this.name,
    seatNumber = this.seatNumber,
    chips = this.chips,
    isDealer = this.isDealer,
    playingStatus = this.playingStatus,
)
