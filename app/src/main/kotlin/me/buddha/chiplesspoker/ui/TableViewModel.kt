package me.buddha.chiplesspoker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.buddha.chiplesspoker.domain.model.BlindLevel
import me.buddha.chiplesspoker.domain.model.BlindStructure
import me.buddha.chiplesspoker.domain.model.Hand
import me.buddha.chiplesspoker.domain.model.Player
import me.buddha.chiplesspoker.domain.model.Table
import me.buddha.chiplesspoker.domain.usecase.DeleteAllUseCase
import me.buddha.chiplesspoker.domain.usecase.DeleteByIdUseCase
import me.buddha.chiplesspoker.domain.usecase.GetAllTablesUseCase
import me.buddha.chiplesspoker.domain.usecase.GetTableByIdUseCase
import me.buddha.chiplesspoker.domain.usecase.InsertOrReplaceTableUseCase
import me.buddha.chiplesspoker.domain.utils.DurationUnit.MINUTES
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.PLAYING
import me.buddha.chiplesspoker.domain.utils.StreetType.PREFLOP
import java.time.LocalDateTime
import javax.inject.Inject

val SAMPLE_Table = Table(
    id = 4,
    dateSaved = LocalDateTime.now(),
    initialBuyIn = 1000,
    street = PREFLOP,
    blindStructure = BlindStructure(
        durationUnit = MINUTES,
        blindLevels = listOf(
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
            isDealer = true,
            playingStatus = PLAYING,
        )
    ),
    currentHand = Hand()
)

@HiltViewModel
class TableViewModel @Inject constructor(
    private val deleteAllUseCase: DeleteAllUseCase,
    private val deleteByIdUseCase: DeleteByIdUseCase,
    private val getAllTablesUseCase: GetAllTablesUseCase,
    private val getTableByIdUseCase: GetTableByIdUseCase,
    private val insertOrReplaceTableUseCase: InsertOrReplaceTableUseCase,
) : ViewModel() {

    init {
        viewModelScope.launch {
            insertTable(SAMPLE_Table)
            delay(10000)
            deleteAllTables()
        }
    }

    private fun deleteAllTables() {
        viewModelScope.launch {
            deleteAllUseCase()
        }
    }

    private fun deleteTable(id: Long) {
        viewModelScope.launch {
            deleteByIdUseCase(id)
        }
    }

    private fun getAllTables() {
        viewModelScope.launch {
            getAllTablesUseCase().collect {

            }
        }
    }

    private fun getTableDetails(id: Long) {
        viewModelScope.launch {
            getTableByIdUseCase(id).collect {

            }
        }
    }

    private fun insertTable(table: Table) {
        viewModelScope.launch {
            insertOrReplaceTableUseCase(table)
        }
    }
}