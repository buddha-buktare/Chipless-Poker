package me.buddha.chiplesspoker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import me.buddha.chiplesspoker.data.model.GameEntity

@Database(entities = [GameEntity::class], version = 1)
abstract class GameDatabase: RoomDatabase() {

    abstract fun gameDao(): GameDao
}