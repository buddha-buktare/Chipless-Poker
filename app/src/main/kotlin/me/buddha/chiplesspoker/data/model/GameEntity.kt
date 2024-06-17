package me.buddha.chiplesspoker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import me.buddha.chiplesspoker.data.converter.Converter
import java.util.Date

@Entity(tableName = "game")
@TypeConverters(Converter::class)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val dateSaved: Date? = null,
    val startingChips: Long,
    val street: StreetType,
    val pot: Long,
    val blindStructure: BlindStructure,
    val players: List<Player>,
    val isAutoSaved: Boolean = false
)
