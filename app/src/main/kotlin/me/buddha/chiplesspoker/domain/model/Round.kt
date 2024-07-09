package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.RoundEntity

data class Round(
    val id: Int = 1,
    val currentMaxBet: Long = 0,
    val endsOn: Int = 0,
    var playersInvestment: List<PlayerInvestment>? = listOf()
)

fun Round.asEntity() = RoundEntity(
    id = this.id,
    currentMaxBet = this.currentMaxBet,
    endsOn = this.endsOn,
    playersInvestment = this.playersInvestment?.map { it.asEntity() },
)
