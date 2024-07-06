package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.HandEntity

data class Hand(
    val dealer: Int = 0,
    val smallBlindPlayer: Int = 1,
    val bigBlindPlayer: Int = 2,
    val decisivePlayer: Int = 2,
    val currentMaxBet: Long = 0,
    val pots: List<Pot> = listOf(),
    val currentPlayer: Int = 3,
)

fun Hand.asEntity() = HandEntity(
    dealer = this.dealer,
    smallBlindPlayer = this.smallBlindPlayer,
    bigBlindPlayer = this.bigBlindPlayer,
    decisivePlayer = this.decisivePlayer,
    currentMaxBet = this.currentMaxBet,
    pots = this.pots.map { it.asEntity() },
    currentPlayer = currentPlayer
)
