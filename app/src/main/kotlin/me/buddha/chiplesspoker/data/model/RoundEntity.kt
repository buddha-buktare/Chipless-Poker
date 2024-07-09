package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.Round

data class RoundEntity(
    val id: Int,
    val currentMaxBet: Long = 0,
    val endsOn: Int = 0,
    val playersInvestment: List<PlayerInvestmentEntity>?
)

fun RoundEntity.asExternalModel() = Round(
    id = this.id,
    currentMaxBet = this.currentMaxBet,
    endsOn = this.endsOn,
    playersInvestment = this.playersInvestment?.map { it.asExternalModel() },
)

