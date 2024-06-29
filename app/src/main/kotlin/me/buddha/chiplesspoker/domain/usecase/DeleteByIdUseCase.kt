package me.buddha.chiplesspoker.domain.usecase

import me.buddha.chiplesspoker.data.repository.TableRepository
import javax.inject.Inject

class DeleteByIdUseCase @Inject constructor(
    private val tableRepository: TableRepository
) {

    suspend operator fun invoke(id: Long) {
        tableRepository.deleteById(id)
    }
}