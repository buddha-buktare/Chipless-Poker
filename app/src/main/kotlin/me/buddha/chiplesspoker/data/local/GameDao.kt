package me.buddha.chiplesspoker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.buddha.chiplesspoker.data.model.GameEntity

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    fun getAllGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :id")
    fun getGameById(id: Long): Flow<GameEntity>

    @Upsert
    fun insertOrReplaceGame(gameEntity: GameEntity)

    @Delete
    fun delete(gameEntity: GameEntity)

    @Query("DELETE FROM games WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM games")
    fun deleteAll()

}