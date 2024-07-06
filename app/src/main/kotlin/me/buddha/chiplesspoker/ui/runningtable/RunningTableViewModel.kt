package me.buddha.chiplesspoker.ui.runningtable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.buddha.chiplesspoker.domain.model.BlindStructure
import me.buddha.chiplesspoker.domain.model.Hand
import me.buddha.chiplesspoker.domain.model.Player
import me.buddha.chiplesspoker.domain.model.Pot
import me.buddha.chiplesspoker.domain.usecase.GetTableByIdUseCase
import me.buddha.chiplesspoker.domain.utils.DurationUnit

@HiltViewModel(assistedFactory = RunningTableViewModel.RunningTableViewModelFactory::class)
class RunningTableViewModel @AssistedInject constructor(
    @Assisted val id: Long,
    private val getTableByIdUseCase: GetTableByIdUseCase,
) : ViewModel() {

    var remainingTime by mutableIntStateOf(0)
    var remainingHands by mutableIntStateOf(0)
    var durationUnit by mutableStateOf(DurationUnit.HANDS)
    var tableId by mutableLongStateOf(id)
    var currentBiggestHand by mutableLongStateOf(0)
    var currentPlayerIndex by mutableIntStateOf(0)
    var nextPlayerIndex by mutableIntStateOf(0)
    var pots = mutableListOf<Pot>()
    var players = mutableListOf<Player>()
    var blindStructure by mutableStateOf(BlindStructure())
    var currentHand by mutableStateOf(Hand())

    init {
        getTableDetails(id)
    }

    private fun getTableDetails(id: Long) {
        viewModelScope.launch {
            getTableByIdUseCase(id).map { table ->
                durationUnit = table.blindStructure.durationUnit
                players = table.players.toMutableList()
                blindStructure = table.blindStructure
                currentHand = table.currentHand
            }
        }
    }

    @AssistedFactory
    interface RunningTableViewModelFactory {
        fun create(id: Long): RunningTableViewModel
    }
}