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
import kotlinx.coroutines.launch
import me.buddha.chiplesspoker.domain.model.BlindStructure
import me.buddha.chiplesspoker.domain.model.Hand
import me.buddha.chiplesspoker.domain.model.Player
import me.buddha.chiplesspoker.domain.model.PlayerInvestment
import me.buddha.chiplesspoker.domain.model.Pot
import me.buddha.chiplesspoker.domain.model.Round
import me.buddha.chiplesspoker.domain.usecase.GetTableByIdUseCase
import me.buddha.chiplesspoker.domain.utils.DurationUnit
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.FOLDED

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
    var currentHand by mutableStateOf<Hand?>(Hand())
    var currentRound by mutableStateOf<Round?>(Round())
    var isTableStarted by mutableStateOf(false)
    var rounds = mutableListOf<Round?>(null)

    init {
        getTableDetails(id)
    }

    private fun getTableDetails(id: Long) {
        viewModelScope.launch {
            getTableByIdUseCase(id).collect { table ->
                durationUnit = table.blindStructure.durationUnit
                players = table.players.toMutableList()
                blindStructure = table.blindStructure
                currentHand = table.currentHand
                currentRound = table.currentHand?.currentRound
                isTableStarted = table.isTableStarted
                if (!isTableStarted) {
                    initiateTable()
                }
            }
        }
    }

    fun initiateTable() {
        currentHand = currentHand?.copy(
            pots = listOf(
                Pot(
                    chips = 0,
                    players = players.map { it.seatNumber },
                )
            ),
            currentRound = Round(
                playersInvestment = players.map { player ->
                    PlayerInvestment(
                        playerSeatNo = player.seatNumber,
                        amount = 0
                    )
                },
                currentMaxBet = blindStructure.blindLevels[0].big
            )
        )
        if (!isRoundStarted()) {
            currentHand?.let { hand ->
                updatePlayerInvestment(hand.smallBlindPlayer, 5)
                updatePlayerInvestment(hand.bigBlindPlayer, 10)
            }
        }
    }

    private fun isRoundStarted(): Boolean {
        return currentHand?.currentRound?.playersInvestment?.run {
            this.sumOf { it.amount } != 0L
        } ?: false
    }

    fun onRoundEnd() {
        // todo distribute the chips to winners
        currentHand?.currentRound?.playersInvestment?.forEach { it.amount = 0 }
    }

    private fun updatePlayerInvestment(seatNumber: Int, chips: Long) {
        players = players.map {
            if (it.seatNumber == seatNumber) {
                it.chips -= chips
            }
            it
        }.toMutableList()

        currentHand?.let { hand ->
            currentHand = hand.copy(
                pots = hand.pots.mapIndexed { index, pot ->
                    if (index == (currentHand?.pots?.size?.minus(1) ?: Long.MAX_VALUE)) {
                        pot.chips += chips
                    }
                    pot
                },
                currentRound = hand.currentRound?.copy(
                    playersInvestment = hand.currentRound?.playersInvestment?.map { player ->
                        if (seatNumber == player.playerSeatNo) {
                            player.amount += chips
                        }
                        player
                    } ?: listOf(),
                    currentMaxBet = hand.currentRound?.currentMaxBet ?: 0
                )
            )
        }
    }

    private fun updateCurrentPlayer() {
        currentHand?.let { hand ->
            currentHand = hand.copy(
                currentPlayer = (hand.currentPlayer + 1) % players.size
            )
        }
    }

    fun onCheck() {
        updateCurrentPlayer()
    }

    fun onCall() {
        currentHand?.let { hand ->
            updatePlayerInvestment(
                seatNumber = hand.currentPlayer,
                chips = hand.currentRound?.currentMaxBet ?: 0
            )
            updateCurrentPlayer()
        }
    }

    fun onBet() {
    }

    fun onRaise() {
    }

    fun onFold() {
        players = players.mapIndexed { index, it ->
            if (index == currentPlayerIndex) {
                it.playingStatus = FOLDED
            }
            it
        }.toMutableList()
    }

    fun onAllIn() {
    }

    @AssistedFactory
    interface RunningTableViewModelFactory {
        fun create(id: Long): RunningTableViewModel
    }
}