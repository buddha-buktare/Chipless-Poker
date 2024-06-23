package me.buddha.chiplesspoker.domain.usecase

import me.buddha.chiplesspoker.data.repository.GameRepository
import javax.inject.Inject

class DeleteByIdUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend operator fun invoke(id: Long) {
        gameRepository.deleteById(id)
    }
}