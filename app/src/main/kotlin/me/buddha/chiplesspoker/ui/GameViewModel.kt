package me.buddha.chiplesspoker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.buddha.chiplesspoker.domain.StreetType.PREFLOP
import me.buddha.chiplesspoker.domain.model.BlindLevel
import me.buddha.chiplesspoker.domain.model.BlindStructure
import me.buddha.chiplesspoker.domain.model.Game
import me.buddha.chiplesspoker.domain.model.Player
import me.buddha.chiplesspoker.domain.model.Pot
import me.buddha.chiplesspoker.domain.usecase.DeleteAllUseCase
import me.buddha.chiplesspoker.domain.usecase.DeleteByIdUseCase
import me.buddha.chiplesspoker.domain.usecase.GetAllGamesUseCase
import me.buddha.chiplesspoker.domain.usecase.GetGameByIdUseCase
import me.buddha.chiplesspoker.domain.usecase.InsertOrReplaceGameUseCase
import java.time.LocalDateTime
import javax.inject.Inject

val SAMPLE_GAME = Game(
    id = 4,
    dateSaved = LocalDateTime.now(),
    initialBuyIn = 1000,
    street = PREFLOP,
    pots = listOf(
        Pot(50000, listOf())
    ),
    blindStructure = BlindStructure(
        durationUnit = "Minutes",
        levels = listOf(
            BlindLevel(
                level = 1,
                big = 6,
                small = 3,
                duration = 30
            )
        )
    ),
    players = listOf(
        Player(
            name = "Buddha",
            seatNumber = 1,
            chips = 199,
            isDealer = true
        )
    )
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val deleteAllUseCase: DeleteAllUseCase,
    private val deleteByIdUseCase: DeleteByIdUseCase,
    private val getAllGamesUseCase: GetAllGamesUseCase,
    private val getGameByIdUseCase: GetGameByIdUseCase,
    private val insertOrReplaceGameUseCase: InsertOrReplaceGameUseCase,
) : ViewModel() {

    init {
        viewModelScope.launch {
            insertGame(SAMPLE_GAME)
            delay(10000)
            deleteAllGames()
        }
    }

    private fun deleteAllGames() {
        viewModelScope.launch {
            deleteAllUseCase()
        }
    }

    private fun deleteGame(id: Long) {
        viewModelScope.launch {
            deleteByIdUseCase(id)
        }
    }

    private fun getAllGames() {
        viewModelScope.launch {
            getAllGamesUseCase().collect {

            }
        }
    }

    private fun getGameDetails(id: Long) {
        viewModelScope.launch {
            getGameByIdUseCase(id).collect {

            }
        }
    }

    private fun insertGame(game: Game) {
        viewModelScope.launch {
            insertOrReplaceGameUseCase(game)
        }
    }
}