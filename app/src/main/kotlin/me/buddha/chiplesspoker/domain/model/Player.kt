package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.PlayerEntity
import me.buddha.chiplesspoker.domain.utils.PlayingStatus
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.PLAYING

data class Player(
    var name: String = "Player",
    var seatNumber: Int = -1,
    var chips: Long = 0,
    val isDealer: Boolean = false,
    val isExited: Boolean = false,
    var playingStatus: PlayingStatus = PLAYING,
)

fun Player.asEntity() = PlayerEntity (
    name = this.name,
    seatNumber = this.seatNumber,
    chips = this.chips,
    isDealer = this.isDealer,
    isExited = this.isExited,
    playingStatus = this.playingStatus,
)