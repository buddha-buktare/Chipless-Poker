package me.buddha.chiplesspoker.data

import kotlinx.coroutines.flow.Flow
import me.buddha.chiplesspoker.data.local.GameDao
import me.buddha.chiplesspoker.data.model.GameEntity
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val gameDao: GameDao
) {

    suspend fun getSavedGames(): Flow<List<GameEntity>> {
        return gameDao.getSavedGames()
    }
}