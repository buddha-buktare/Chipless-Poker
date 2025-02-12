package me.buddha.chiplesspoker.data.repository

import kotlinx.coroutines.flow.Flow
import me.buddha.chiplesspoker.data.local.TableDao
import me.buddha.chiplesspoker.data.model.TableEntity
import javax.inject.Inject

class TableRepositoryImpl @Inject constructor(
    private val tableDao: TableDao
) : TableRepository {
    override fun getAllTables(): Flow<List<TableEntity>> {
        return tableDao.getAllTables()
    }

    override fun getTableById(id: Long): Flow<TableEntity> {
        return tableDao.getTableById(id)
    }

    override suspend fun insertOrReplaceTable(tableEntity: TableEntity): Long {
        return tableDao.insertOrReplaceTable(tableEntity)
    }

    override suspend fun delete(tableEntity: TableEntity) {
        tableDao.delete(tableEntity)
    }

    override suspend fun deleteById(id: Long) {
        tableDao.deleteById(id)
    }

    override suspend fun deleteAll() {
        tableDao.deleteAll()
    }
}