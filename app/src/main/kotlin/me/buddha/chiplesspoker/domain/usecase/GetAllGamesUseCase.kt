package me.buddha.chiplesspoker.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.buddha.chiplesspoker.data.model.asExternalModel
import me.buddha.chiplesspoker.data.repository.TableRepository
import me.buddha.chiplesspoker.domain.model.Table
import javax.inject.Inject

class GetAllTablesUseCase @Inject constructor(
    private val tableRepository: TableRepository
) {

    operator fun invoke(): Flow<List<Table>> {
        return tableRepository.getAllTables().map { list ->
            list.map { it.asExternalModel() }
        }
    }
}