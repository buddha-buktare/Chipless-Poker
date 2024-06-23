package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.Pot

data class PotEntity(
    val chips: Long,
    val players: List<Long>,
)

fun PotEntity.asExternalModel() = Pot(
    chips = this.chips,
    players = this.players,
)