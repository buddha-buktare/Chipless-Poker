package me.buddha.chiplesspoker.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.buddha.chiplesspoker.domain.usecase.DeleteAllUseCase
import me.buddha.chiplesspoker.domain.usecase.DeleteByIdUseCase
import me.buddha.chiplesspoker.domain.usecase.GetAllGamesUseCase
import me.buddha.chiplesspoker.domain.usecase.GetGameByIdUseCase
import me.buddha.chiplesspoker.domain.usecase.InsertOrReplaceGameUseCase
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val deleteAllUseCase: DeleteAllUseCase,
    private val deleteByIdUseCase: DeleteByIdUseCase,
    private val getAllGamesUseCase: GetAllGamesUseCase,
    private val getGameByIdUseCase: GetGameByIdUseCase,
    private val insertOrReplaceGameUseCase: InsertOrReplaceGameUseCase,
) : ViewModel() {

    // init {
    //     viewModelScope.launch {
    //         insertOrReplaceGameUseCase(
    //             Game(
    //                 id = 1,
    //                 dateSaved = LocalDateTime.now(),
    //                 initialBuyIn = 1000,
    //                 street = FLOP,
    //                 pot = 50,
    //                 blindStructure = BlindStructure(
    //                     durationUnit = "Minutes",
    //                     levels = listOf(
    //                         BlindLevel(
    //                             level = 1,
    //                             big = 6,
    //                             small = 3,
    //                             duration = 30
    //                         )
    //                     )
    //                 ),
    //                 players = listOf(
    //                     Player(
    //                         name = "Buddha",
    //                         seatNumber = 1,
    //                         chips = 199,
    //                         isDealer = true
    //                     )
    //                 )
    //             )
    //         )
    //     }
    // }
}