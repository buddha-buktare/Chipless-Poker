package me.buddha.chiplesspoker.ui.createtable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.buddha.chiplesspoker.domain.model.BlindLevel
import me.buddha.chiplesspoker.domain.model.BlindStructure
import me.buddha.chiplesspoker.domain.model.Hand
import me.buddha.chiplesspoker.domain.model.Player
import me.buddha.chiplesspoker.domain.model.Table
import me.buddha.chiplesspoker.domain.navigation.Destination.RunningTable
import me.buddha.chiplesspoker.domain.navigation.NavigationService
import me.buddha.chiplesspoker.domain.usecase.InsertOrReplaceTableUseCase
import me.buddha.chiplesspoker.domain.utils.DurationUnit
import me.buddha.chiplesspoker.domain.utils.DurationUnit.HANDS
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.EMPTY
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.PLAYING
import me.buddha.chiplesspoker.domain.utils.StreetType.PREFLOP
import javax.inject.Inject

@HiltViewModel
class CreateTableViewModel @Inject constructor(
    private val insertOrReplaceTableUseCase: InsertOrReplaceTableUseCase,
    private val navigationService: NavigationService
) : ViewModel() {

    var initialBuyInAmount by mutableLongStateOf(1000L)
    var blindStructure by mutableStateOf(BlindStructure())
    var players = mutableStateListOf<Player>().apply {
        repeat(6) { add(Player(playingStatus = EMPTY)) }
    }


    fun updateInitialBuyIn(buyIn: Long) {
        initialBuyInAmount = buyIn
    }

    fun updateDurationUnit(unit: DurationUnit) {
        blindStructure.durationUnit = unit
    }

    fun addBlindLevel() {
        val newLevel = BlindLevel(
            level = blindStructure.blindLevels.size + 1,
            big = (blindStructure.blindLevels.getOrNull(blindStructure.blindLevels.size - 1)?.big
                ?: 5) * 2,
            small = (blindStructure.blindLevels.getOrNull(blindStructure.blindLevels.size - 1)?.small
                ?: 2) * 2,
            duration = -1,
        )
        blindStructure = blindStructure.copy(
            blindLevels = blindStructure.blindLevels + newLevel
        )
    }

    fun updateLevelBigBlind(levelIndex: Int, bigBlind: Long) {
        blindStructure = blindStructure.copy(
            blindLevels = blindStructure.blindLevels.mapIndexed { index, level ->
                if (index == levelIndex) {
                    level.copy(big = bigBlind)
                } else {
                    level
                }
            }
        )
    }

    fun updateLevelSmallBlind(levelIndex: Int, smallBlind: Long) {
        blindStructure = blindStructure.copy(
            blindLevels = blindStructure.blindLevels.mapIndexed { index, level ->
                if (index == levelIndex) {
                    level.copy(small = smallBlind)
                } else {
                    level
                }
            }
        )
    }

    fun updateLevelDuration(levelIndex: Int, duration: Long) {
        blindStructure = blindStructure.copy(
            blindLevels = blindStructure.blindLevels.mapIndexed { index, level ->
                if (index == levelIndex) {
                    level.copy(duration = duration)
                } else {
                    level
                }
            }
        )
    }

    fun removeBlindLevel(levelIndex: Int) {
        blindStructure = blindStructure.copy(
            blindLevels = blindStructure.blindLevels.filterIndexed { index, _ -> index != levelIndex }
        )
    }

    fun addPlayer(index: Int, name: String) {
        players[index] = Player(
            name = name,
            seatNumber = index,
            playingStatus = PLAYING,
            chips = initialBuyInAmount,
        )
    }

    fun updatePlayer(index: Int, name: String) {
        players[index].name = name
    }

    fun removePlayer(index: Int) {
        players[index] = Player()
    }

    fun startTable() {
        viewModelScope.launch {
            players = players.sortedBy { it.seatNumber }.toMutableStateList()
            val id = insertOrReplaceTableUseCase(
                Table(
                    initialBuyIn = initialBuyInAmount,
                    street = PREFLOP,
                    blindStructure = BlindStructure(
                        durationUnit = HANDS,
                        blindLevels = listOf(
                            BlindLevel(
                                level = 1,
                                big = 10,
                                small = 5,
                                duration = -1
                            )
                        )
                    ),
                    players = players,
                    currentHand = getHandDetails(players.filter { it.seatNumber != -1 }[0].seatNumber)
                )
            )
            navigationService.navController.navigate(RunningTable(id = id))
        }
    }

    fun getHandDetails(dealerIndex: Int): Hand {
        val playingList = players.filter { it.playingStatus == PLAYING }.sortedBy { it.seatNumber }
        val smallBlindIndex = if (playingList.size == 2) {
            dealerIndex
        } else {
            (dealerIndex + 1) % playingList.size
        }
        val bigBlindIndex = if (playingList.size == 2) {
            (dealerIndex + 1) % playingList.size
        } else {
            (dealerIndex + 2) % playingList.size
        }
        val currentPlayer = (bigBlindIndex + 1) % playingList.size

        return Hand(
            dealer = playingList[dealerIndex].seatNumber,
            smallBlindPlayer = playingList[smallBlindIndex].seatNumber,
            bigBlindPlayer = playingList[bigBlindIndex].seatNumber,
            currentPlayer = playingList[currentPlayer].seatNumber
        )
    }
}