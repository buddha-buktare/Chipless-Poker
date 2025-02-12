package me.buddha.chiplesspoker.data.repository

import kotlinx.coroutines.flow.Flow
import me.buddha.chiplesspoker.data.model.TableEntity

interface TableRepository {
    fun getAllTables(): Flow<List<TableEntity>>

    fun getTableById(id: Long): Flow<TableEntity>

    suspend fun insertOrReplaceTable(tableEntity: TableEntity): Long

    suspend fun delete(tableEntity: TableEntity)

    suspend fun deleteById(id: Long)

    suspend fun deleteAll()
}