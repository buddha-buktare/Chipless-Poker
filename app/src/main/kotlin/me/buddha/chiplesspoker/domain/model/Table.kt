package me.buddha.chiplesspoker.domain.model

import me.buddha.chiplesspoker.data.model.TableEntity
import me.buddha.chiplesspoker.domain.StreetType
import java.time.LocalDateTime

data class Table(
    val id: Long? = null,
    val dateSaved: LocalDateTime? = null,
    val initialBuyIn: Long,
    val street: StreetType,
    val pots: List<Pot>,
    val blindStructure: BlindStructure,
    val players: List<Player>,
    val isAutoSaved: Boolean = false
)

fun Table.asEntity() = TableEntity(
    id = this.id,
    dateSaved = this.dateSaved,
    initialBuyIn = this.initialBuyIn,
    street = this.street,
    pots = this.pots.map { it.asEntity() },
    blindStructure = this.blindStructure.asEntity(),
    players = this.players.map { it.asEntity() },
    isAutoSaved = isAutoSaved
)
