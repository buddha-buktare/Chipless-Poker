package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.HandEntity

data class Hand(
    val index: Int = 1,
    val dealer: Int = 0,
    val smallBlindPlayer: Int = 1,
    val bigBlindPlayer: Int = 2,
    val decisivePlayer: Int = 2,
    var previousBet: Long = 0,
    var pots: List<Pot> = listOf(),
    val currentPlayer: Int = 3,
    var currentRound: Round? = null,
    val endOnBigBlind: Boolean = true,
)

fun Hand.asEntity() = HandEntity(
    index = this.index,
    dealer = this.dealer,
    smallBlindPlayer = this.smallBlindPlayer,
    bigBlindPlayer = this.bigBlindPlayer,
    decisivePlayer = this.decisivePlayer,
    previousBet = this.previousBet,
    pots = this.pots.map { it.asEntity() },
    currentPlayer = currentPlayer,
    currentRound = this.currentRound?.asEntity()
)
