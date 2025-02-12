package me.buddha.chiplesspoker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.buddha.chiplesspoker.data.converter.Converters
import me.buddha.chiplesspoker.data.model.TableEntity

@Database(
    entities = [TableEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class TableDatabase : RoomDatabase() {

    abstract fun tableDao(): TableDao
}