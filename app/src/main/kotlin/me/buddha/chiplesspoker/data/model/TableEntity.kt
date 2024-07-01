package me.buddha.chiplesspoker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import me.buddha.chiplesspoker.data.converter.Converters
import me.buddha.chiplesspoker.domain.StreetType
import me.buddha.chiplesspoker.domain.model.Table
import java.time.LocalDateTime

@Entity(tableName = "tables")
@TypeConverters(Converters::class)
data class TableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val dateSaved: LocalDateTime? = null,
    val initialBuyIn: Long,
    val street: StreetType,
    val pots: List<PotEntity>,
    val blindStructure: BlindStructureEntity,
    val players: List<PlayerEntity>,
    val isAutoSaved: Boolean = false
)

fun TableEntity.asExternalModel() = Table(
    id = this.id,
    dateSaved = this.dateSaved,
    initialBuyIn = this.initialBuyIn,
    street = this.street,
    pots = this.pots.map { it.asExternalModel() },
    blindStructure = this.blindStructure.asExternalModel(),
    players = this.players.map { it.asExternalModel() },
    isAutoSaved = isAutoSaved
)
