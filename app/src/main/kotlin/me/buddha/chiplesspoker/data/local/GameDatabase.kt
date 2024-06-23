package me.buddha.chiplesspoker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.buddha.chiplesspoker.data.converter.Converters
import me.buddha.chiplesspoker.data.model.GameEntity

@Database(
    entities = [GameEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class GameDatabase: RoomDatabase() {

    abstract fun gameDao(): GameDao
}