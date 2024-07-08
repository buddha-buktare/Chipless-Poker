package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.PlayerInvestmentEntity

data class PlayerInvestment(
    val playerSeatNo: Int,
    var amount: Long,
)

fun PlayerInvestment.asEntity() = PlayerInvestmentEntity(
    playerSeatNo = this.playerSeatNo,
    amount = this.amount,
)