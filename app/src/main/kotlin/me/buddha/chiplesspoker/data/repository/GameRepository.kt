package me.buddha.chiplesspoker.data.repository

import kotlinx.coroutines.flow.Flow
import me.buddha.chiplesspoker.data.model.GameEntity

interface GameRepository {
    fun getAllGames(): Flow<List<GameEntity>>

    fun getGameById(id: Long):Flow<GameEntity>

    fun insertOrReplaceGame(gameEntity: GameEntity)

    fun delete(gameEntity: GameEntity)

    fun deleteById(id: Long)
    fun deleteAll()
}