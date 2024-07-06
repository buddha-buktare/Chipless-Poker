package me.buddha.chiplesspoker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import me.buddha.chiplesspoker.data.converter.Converters
import me.buddha.chiplesspoker.domain.model.Table
import me.buddha.chiplesspoker.domain.utils.StreetType
import java.time.LocalDateTime

@Entity(tableName = "tables")
@TypeConverters(Converters::class)
data class TableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val dateSaved: LocalDateTime? = null,
    val initialBuyIn: Long,
    val street: StreetType,
    val blindStructure: BlindStructureEntity,
    val players: List<PlayerEntity>,
    val currentHand: HandEntity,
    val isAutoSaved: Boolean = false
)

fun TableEntity.asExternalModel() = Table(
    id = this.id,
    dateSaved = this.dateSaved,
    initialBuyIn = this.initialBuyIn,
    street = this.street,
    blindStructure = this.blindStructure.asExternalModel(),
    players = this.players.map { it.asExternalModel() },
    currentHand = this.currentHand.asExternalModel(),
    isAutoSaved = isAutoSaved
)
