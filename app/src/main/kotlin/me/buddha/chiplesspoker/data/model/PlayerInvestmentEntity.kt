package me.buddha.chiplesspoker.data.model

import me.buddha.chiplesspoker.domain.model.PlayerInvestment
import me.buddha.chiplesspoker.domain.utils.PlayerMove

data class PlayerInvestmentEntity(
    val playerSeatNo: Int,
    val move: PlayerMove,
    val amount: Long,
)

fun PlayerInvestmentEntity.asExternalModel() = PlayerInvestment(
    playerSeatNo = this.playerSeatNo,
    move = this.move,
    amount = this.amount,
)


