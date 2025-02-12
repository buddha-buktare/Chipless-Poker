package me.buddha.chiplesspoker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.buddha.chiplesspoker.data.model.TableEntity

@Dao
interface TableDao {
    @Query("SELECT * FROM tables")
    fun getAllTables(): Flow<List<TableEntity>>

    @Query("SELECT * FROM tables WHERE id = :id")
    fun getTableById(id: Long): Flow<TableEntity>

    @Upsert
    suspend fun insertOrReplaceTable(tableEntity: TableEntity): Long

    @Delete
    suspend fun delete(tableEntity: TableEntity)

    @Query("DELETE FROM tables WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM tables")
    suspend fun deleteAll()
}