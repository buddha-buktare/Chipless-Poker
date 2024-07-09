package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.Hand

data class HandEntity(
    val index: Int,
    val dealer: Int,
    val smallBlindPlayer: Int,
    val bigBlindPlayer: Int,
    val decisivePlayer: Int,
    val pots: List<PotEntity>,
    val currentPlayer: Int,
    val currentRound: RoundEntity? = null,
    val endOnBigBlind: Boolean = true,
)

fun HandEntity.asExternalModel() = Hand(
    index = this.index,
    dealer = this.dealer,
    smallBlindPlayer = this.smallBlindPlayer,
    bigBlindPlayer = this.bigBlindPlayer,
    decisivePlayer = this.decisivePlayer,
    pots = this.pots.map { it.asExternalModel() },
    currentPlayer = currentPlayer,
    currentRound = this.currentRound?.asExternalModel(),
    endOnBigBlind = this.endOnBigBlind,
)

