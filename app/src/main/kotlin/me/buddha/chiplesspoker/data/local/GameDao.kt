package me.buddha.chiplesspoker.data.local

import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.buddha.chiplesspoker.data.model.GameEntity

interface GameDao {
    @Query("SELECT * FROM game")
    suspend fun getSavedGames(): Flow<List<GameEntity>>


}