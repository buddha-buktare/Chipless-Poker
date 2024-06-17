package me.buddha.chiplesspoker.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.buddha.chiplesspoker.data.model.asExternalModel
import me.buddha.chiplesspoker.data.repository.GameRepository
import me.buddha.chiplesspoker.domain.model.Game
import javax.inject.Inject

class GetAllGamesUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    operator fun invoke(): Flow<List<Game>> {
        return gameRepository.getAllGames().map { list ->
            list.map { it.asExternalModel() }
        }
    }
}