package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.PotEntity

data class Pot(
    var chips: Long = 0,
    val players: List<Int>,
)

fun Pot.asEntity() = PotEntity(
    chips = this.chips,
    players = this.players,
)
