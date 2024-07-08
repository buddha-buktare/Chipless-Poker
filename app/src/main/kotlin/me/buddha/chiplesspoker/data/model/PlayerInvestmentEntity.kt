package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.PlayerInvestment

data class PlayerInvestmentEntity(
    val playerSeatNo: Int,
    val amount: Long,
)

fun PlayerInvestmentEntity.asExternalModel() = PlayerInvestment(
    playerSeatNo = this.playerSeatNo,
    amount = this.amount,
)


