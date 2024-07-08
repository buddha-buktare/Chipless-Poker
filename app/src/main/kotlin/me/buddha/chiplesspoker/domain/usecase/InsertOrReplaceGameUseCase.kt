package me.buddha.chiplesspoker.domain.usecase

import me.buddha.chiplesspoker.data.repository.TableRepository
import me.buddha.chiplesspoker.domain.model.Table
import me.buddha.chiplesspoker.domain.model.asEntity
import javax.inject.Inject

class InsertOrReplaceTableUseCase @Inject constructor(
    private val tableRepository: TableRepository
) {

    suspend operator fun invoke(table: Table): Long {
        return tableRepository.insertOrReplaceTable(
            table.asEntity()
        )
    }
}