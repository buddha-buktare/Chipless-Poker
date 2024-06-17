package me.buddha.chiplesspoker.domain.usecase

import me.buddha.chiplesspoker.data.repository.GameRepository
import javax.inject.Inject

class DeleteAllUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    operator fun invoke() {
        gameRepository.deleteAll()
    }
}