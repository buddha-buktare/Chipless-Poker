package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.PotEntity

data class Pot(
    val chips: Long,
    val players: List<Long>,
)

fun Pot.asEntity() = PotEntity(
    chips = this.chips,
    players = this.players,
)
