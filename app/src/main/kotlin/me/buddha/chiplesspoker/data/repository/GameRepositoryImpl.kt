package me.buddha.chiplesspoker.data.repository

import kotlinx.coroutines.flow.Flow
import me.buddha.chiplesspoker.data.local.GameDao
import me.buddha.chiplesspoker.data.model.GameEntity
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val gameDao: GameDao
): GameRepository {
    override fun getAllGames(): Flow<List<GameEntity>> {
        return gameDao.getAllGames()
    }

    override fun getGameById(id: Long): Flow<GameEntity> {
        return gameDao.getGameById(id)
    }

    override suspend fun insertOrReplaceGame(gameEntity: GameEntity) {
        return gameDao.insertOrReplaceGame(gameEntity)
    }

    override suspend fun delete(gameEntity: GameEntity) {
        gameDao.delete(gameEntity)
    }

    override suspend fun deleteById(id: Long) {
        gameDao.deleteById(id)
    }

    override suspend fun deleteAll() {
        gameDao.deleteAll()
    }
}