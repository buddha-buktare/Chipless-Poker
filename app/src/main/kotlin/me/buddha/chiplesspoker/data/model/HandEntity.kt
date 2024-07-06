package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.Hand

data class HandEntity(
    val dealer: Int,
    val smallBlindPlayer: Int,
    val bigBlindPlayer: Int,
    val decisivePlayer: Int,
    val currentMaxBet: Long,
    val pots: List<PotEntity>,
    val currentPlayer: Int,
)

fun HandEntity.asExternalModel() = Hand(
    dealer = this.dealer,
    smallBlindPlayer = this.smallBlindPlayer,
    bigBlindPlayer = this.bigBlindPlayer,
    decisivePlayer = this.decisivePlayer,
    currentMaxBet = this.currentMaxBet,
    pots = this.pots.map { it.asExternalModel() },
    currentPlayer = currentPlayer
)

