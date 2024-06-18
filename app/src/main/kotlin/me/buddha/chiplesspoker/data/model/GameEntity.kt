package me.buddha.chiplesspoker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import me.buddha.chiplesspoker.data.converter.Converters
import me.buddha.chiplesspoker.domain.StreetType
import me.buddha.chiplesspoker.domain.model.Game
import java.time.LocalDateTime

@Entity(tableName = "games")
@TypeConverters(Converters::class)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val dateSaved: LocalDateTime? = null,
    val initialBuyIn: Long,
    val street: StreetType,
    val pot: Long,
    val blindStructure: BlindStructureEntity,
    val players: List<PlayerEntity>,
    val isAutoSaved: Boolean = false
)

fun GameEntity.asExternalModel() = Game (
    id = this.id,
    dateSaved = this.dateSaved,
    initialBuyIn = this.initialBuyIn,
    street = this.street,
    pot = this.pot,
    blindStructure = this.blindStructure.asExternalModel(),
    players = this.players.map { it.asExternalModel() },
    isAutoSaved = isAutoSaved
)
