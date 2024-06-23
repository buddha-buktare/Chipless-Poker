package me.buddha.chiplesspoker.domain.usecase

import me.buddha.chiplesspoker.data.repository.GameRepository
import me.buddha.chiplesspoker.domain.model.Game
import me.buddha.chiplesspoker.domain.model.asEntity
import javax.inject.Inject

class InsertOrReplaceGameUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend operator fun invoke(game: Game) {
        return gameRepository.insertOrReplaceGame(
            game.asEntity()
        )
    }
}