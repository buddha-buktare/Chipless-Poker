package me.buddha.chiplesspoker.ui.createtable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.buddha.chiplesspoker.domain.StreetType.PREFLOP
import me.buddha.chiplesspoker.domain.model.BlindLevel
import me.buddha.chiplesspoker.domain.model.BlindStructure
import me.buddha.chiplesspoker.domain.model.Player
import me.buddha.chiplesspoker.domain.model.Table
import me.buddha.chiplesspoker.domain.usecase.DurationUnit
import me.buddha.chiplesspoker.domain.usecase.InsertOrReplaceTableUseCase
import javax.inject.Inject

@HiltViewModel
class CreateTableViewModel @Inject constructor(
    private val insertOrReplaceTableUseCase: InsertOrReplaceTableUseCase,
) : ViewModel() {

    var initialBuyInAmount by mutableLongStateOf(1000L)
    var blindStructure by mutableStateOf(BlindStructure())
    var players = mutableStateListOf<Player>()

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
        players.add(Player(name = name, seatNumber = index))
    }

    fun updatePlayer(index: Int, name: String) {
        players.toMutableList().firstOrNull { it.seatNumber == index }?.name = name
    }

    fun removePlayer(index: Int) {
        players.removeAt(index)
    }

    fun startTable() {
        viewModelScope.launch {
            insertOrReplaceTableUseCase(
                Table(
                    initialBuyIn = initialBuyInAmount,
                    street = PREFLOP,
                    pots = listOf(),
                    blindStructure = blindStructure,
                    players = players,
                )
            )
        }
    }
}