package me.buddha.chiplesspoker.data.repository

import kotlinx.coroutines.flow.Flow
import me.buddha.chiplesspoker.data.model.GameEntity

interface GameRepository {
    fun getAllGames(): Flow<List<GameEntity>>

    fun getGameById(id: Long):Flow<GameEntity>

    suspend fun insertOrReplaceGame(gameEntity: GameEntity)

    suspend fun delete(gameEntity: GameEntity)

    suspend fun deleteById(id: Long)

    suspend fun deleteAll()
}