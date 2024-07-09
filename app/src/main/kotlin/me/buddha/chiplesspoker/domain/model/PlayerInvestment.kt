package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.PlayerInvestmentEntity
import me.buddha.chiplesspoker.domain.utils.PlayerMove
import me.buddha.chiplesspoker.domain.utils.PlayerMove.EMPTY

data class PlayerInvestment(
    val playerSeatNo: Int,
    var move: PlayerMove = EMPTY,
    var amount: Long,
)

fun PlayerInvestment.asEntity() = PlayerInvestmentEntity(
    playerSeatNo = this.playerSeatNo,
    move = this.move,
    amount = this.amount,
)