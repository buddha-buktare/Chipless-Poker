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
import me.buddha.chiplesspoker.domain.utils.PlayerMove
import me.buddha.chiplesspoker.domain.utils.PlayerMove.ALL_IN
import me.buddha.chiplesspoker.domain.utils.PlayerMove.BB
import me.buddha.chiplesspoker.domain.utils.PlayerMove.BET
import me.buddha.chiplesspoker.domain.utils.PlayerMove.CALL
import me.buddha.chiplesspoker.domain.utils.PlayerMove.CHECK
import me.buddha.chiplesspoker.domain.utils.PlayerMove.FOLD
import me.buddha.chiplesspoker.domain.utils.PlayerMove.RAISE
import me.buddha.chiplesspoker.domain.utils.PlayerMove.SB
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.FOLDED
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.PLAYING
import me.buddha.chiplesspoker.domain.utils.StreetType
import me.buddha.chiplesspoker.domain.utils.StreetType.PREFLOP

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
    var currentStreet by mutableStateOf<StreetType>(PREFLOP)
    var isTableStarted by mutableStateOf(false)
    var callAmount by mutableStateOf(0L)
    var actionsForCurrentPlayer = mutableListOf<PlayerMove>()
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
                currentStreet = table.street
                isTableStarted = table.isTableStarted
                if (!isTableStarted) {
                    initiateTable()
                }
                getActionsForCurrentPlayer()
            }
        }
    }

    private fun initiateTable() {
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
                currentMaxBet = blindStructure.blindLevels[blindStructure.currentLevel].big,
            ),
        )
        if (!isRoundStarted()) {
            currentHand?.let { hand ->
                updatePlayerInvestment(
                    seatNumber = hand.smallBlindPlayer,
                    chips = blindStructure.blindLevels[blindStructure.currentLevel].small,
                    move = SB
                )
                updatePlayerInvestment(
                    seatNumber = hand.bigBlindPlayer,
                    chips = blindStructure.blindLevels[blindStructure.currentLevel].big,
                    move = BB
                )
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

    private fun updatePlayerInvestment(seatNumber: Int, chips: Long, move: PlayerMove) {
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
                            player.move = move
                        }
                        player
                    } ?: listOf(),
                    currentMaxBet = hand.currentRound?.currentMaxBet ?: 0
                )
            )
        }
    }

    private fun updateCurrentPlayer() {
        val currentPlayer = currentHand?.currentPlayer
        val playingList =
            players.filter { it.playingStatus == PLAYING }.map { it.seatNumber }.sorted()

        val currentIndexInPlayingList = playingList.indexOf(currentPlayer)
        val newPlayer = playingList[(currentIndexInPlayingList + 1) % playingList.size]

        currentHand?.let { hand ->
            currentHand = hand.copy(
                currentPlayer = newPlayer
            )
        }
        getActionsForCurrentPlayer()
    }

    private fun getActionsForCurrentPlayer() {
        currentHand?.let { hand ->
            val currentPlayer = hand.currentPlayer
            val currentMaxBet = hand.currentRound?.currentMaxBet ?: 0
            val investedAmount =
                hand.currentRound?.playersInvestment?.firstOrNull { it.playerSeatNo == hand.currentPlayer }?.amount
                    ?: 0
            val playerAmount = players.firstOrNull { it.seatNumber == currentPlayer }?.chips ?: 0

            val actions = mutableListOf<PlayerMove>()

            callAmount = currentMaxBet - investedAmount

            if (callAmount == 0L) {
                actions.add(CHECK)
                actions.add(BET)
            } else {
                if (currentMaxBet >= (playerAmount - investedAmount)) {
                    actions.add(ALL_IN)
                } else {
                    actions.add(CALL)
                    actions.add(RAISE)
                }
            }
            actions.add(FOLD)
            actionsForCurrentPlayer.clear()
            actionsForCurrentPlayer = actions.toMutableList()
        }
    }

    fun onCheck() {
        currentHand?.let { hand ->
            updatePlayerInvestment(
                seatNumber = hand.currentPlayer,
                chips = 0,
                move = CHECK
            )
            updateCurrentPlayer()
        }
    }

    fun onCall() {
        currentHand?.let { hand ->
            val investedAmount =
                hand.currentRound?.playersInvestment?.firstOrNull { it.playerSeatNo == hand.currentPlayer }?.amount
                    ?: 0

            hand.currentRound?.currentMaxBet?.let { callAmount ->
                updatePlayerInvestment(
                    seatNumber = hand.currentPlayer,
                    chips = (callAmount - investedAmount),
                    move = CALL
                )
            }
            updateCurrentPlayer()
        }
    }

    fun onBet(amount: Long) {
        currentHand?.let { hand ->
            updatePlayerInvestment(
                seatNumber = hand.currentPlayer,
                chips = amount,
                move = BET
            )
            updateCurrentPlayer()
        }
    }

    fun onRaise(amount: Long) {
        currentHand?.let { hand ->
            updatePlayerInvestment(
                seatNumber = hand.currentPlayer,
                chips = amount,
                move = RAISE
            )
            updateCurrentPlayer()
        }
    }

    fun onFold() {
        players = players.map { player ->
            if (player.seatNumber == currentHand?.currentPlayer) {
                player.playingStatus = FOLDED
            }
            player
        }.toMutableList()
        currentHand?.let { hand ->
            updatePlayerInvestment(
                seatNumber = hand.currentPlayer,
                chips = 0,
                move = FOLD
            )
        }
        updateCurrentPlayer()
    }

    fun onAllIn() {
    }

    /**
     * Takes the Id of the table as a parameter for opening the specific table
     */
    @AssistedFactory
    interface RunningTableViewModelFactory {
        fun create(id: Long): RunningTableViewModel
    }
}

