package me.buddha.chiplesspoker.ui.runningtable

import androidx.compose.runtime.getValue
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
import me.buddha.chiplesspoker.domain.utils.PlayerMove
import me.buddha.chiplesspoker.domain.utils.PlayerMove.ALL_IN
import me.buddha.chiplesspoker.domain.utils.PlayerMove.BB
import me.buddha.chiplesspoker.domain.utils.PlayerMove.BET
import me.buddha.chiplesspoker.domain.utils.PlayerMove.CALL
import me.buddha.chiplesspoker.domain.utils.PlayerMove.CHECK
import me.buddha.chiplesspoker.domain.utils.PlayerMove.EMPTY
import me.buddha.chiplesspoker.domain.utils.PlayerMove.FOLD
import me.buddha.chiplesspoker.domain.utils.PlayerMove.RAISE
import me.buddha.chiplesspoker.domain.utils.PlayerMove.SB
import me.buddha.chiplesspoker.domain.utils.PlayingStatus
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.ALL_IN_ACKNOWLEDGED
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.FOLDED
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.PLAYING
import me.buddha.chiplesspoker.domain.utils.StreetType.FLOP
import me.buddha.chiplesspoker.domain.utils.StreetType.PREFLOP
import me.buddha.chiplesspoker.domain.utils.StreetType.RIVER
import me.buddha.chiplesspoker.domain.utils.StreetType.TURN

@HiltViewModel(assistedFactory = RunningTableViewModel.RunningTableViewModelFactory::class)
class RunningTableViewModel @AssistedInject constructor(
    @Assisted val id: Long,
    private val getTableByIdUseCase: GetTableByIdUseCase,
) : ViewModel() {

    var players = mutableListOf<Player>()
    var blindStructure by mutableStateOf(BlindStructure())
    var currentHand by mutableStateOf<Hand?>(Hand())
    var currentStreet by mutableStateOf(PREFLOP)
    var isTableStarted by mutableStateOf(false)
    var callAmount by mutableStateOf(0L)
    var actionsForCurrentPlayer = mutableListOf<PlayerMove>()
    var winners = mutableListOf<MutableList<Int>>(listOf<Int>().toMutableList())

    var showPotDetails by mutableStateOf(false)

    init {
        getTableDetails(id)
    }

    private fun getTableDetails(id: Long) {
        viewModelScope.launch {
            getTableByIdUseCase(id).collect { table ->
                players = table.players.toMutableList()
                blindStructure = table.blindStructure
                currentHand = table.currentHand
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
                    players = players.filter { it.playingStatus == PLAYING }.map { it.seatNumber },
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
                endsOn = currentHand?.bigBlindPlayer ?: 0
            ),
        )

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

        isTableStarted = true
    }

    private fun onRoundEnd() {
        currentHand?.let { hand ->
            /**
             * This list filters the list of all the [ALL-IN] players in the current round.
             */
            val allInList = hand.currentRound?.playersInvestment?.filter { it.move == ALL_IN }
                ?.sortedBy { it.amount }

            allInList?.forEach { playerInvestment ->
                val playerAmountForAllIn = playerInvestment.amount
                var amountToTakeOutFromMainPot = 0L

                /**
                 * Here we iterate over each of the [ALL-IN] elements and
                 * 1. Add the eligible players for the current pot.
                 * 2. Create a new pot excluding the current [ALL-IN] player
                 */

                currentHand = hand.copy(
                    currentRound = hand.currentRound?.copy(
                        playersInvestment = hand.currentRound?.playersInvestment?.map { player ->
                            if (
                                players.firstOrNull { it.seatNumber == player.playerSeatNo }?.playingStatus == PLAYING ||
                                players.firstOrNull { it.seatNumber == player.playerSeatNo }?.playingStatus == PlayingStatus.ALL_IN
                            ) {
                                if (player.amount >= playerAmountForAllIn) {
                                    amountToTakeOutFromMainPot += (player.amount - playerAmountForAllIn)
                                    player.copy(
                                        amount = player.amount - playerAmountForAllIn,
                                    )
                                } else {
                                    player
                                }
                            } else {
                                player
                            }
                        }
                    )
                )

                players = players.map { player ->
                    if (player.seatNumber == playerInvestment.playerSeatNo) {
                        player.copy(playingStatus = ALL_IN_ACKNOWLEDGED)
                    } else {
                        player
                    }
                }.toMutableList()

                /**
                 * Finally, we update the pot structure
                 */
                currentHand = hand.copy(
                    pots = hand.pots.mapIndexed { index, pot ->
                        if (index == hand.pots.size - 1) {
                            pot.copy(
                                chips = pot.chips - amountToTakeOutFromMainPot,
                            )
                        } else {
                            pot
                        }
                    } + Pot(
                        chips = amountToTakeOutFromMainPot,
                        players = players.filter { it.playingStatus == PLAYING }
                            .map { it.seatNumber }
                    )
                )
            }

            currentHand = hand.copy(
                previousBet = 0,
                currentPlayer = getEndsOnPlayer(),
                currentRound = hand.currentRound?.copy(
                    currentMaxBet = 0,
                    endsOn = getEndsOnPlayer(),
                    playersInvestment = hand.currentRound?.playersInvestment?.map { player ->
                        player.copy(
                            move = EMPTY,
                            amount = 0,
                        )
                    }
                ),
            )

            if (players.filter { it.playingStatus == PLAYING }.size <= 1) {
                onHandEnd()
            }

            clearRoundData()

            when (currentStreet) {
                PREFLOP -> currentStreet = FLOP
                FLOP -> currentStreet = TURN
                TURN -> currentStreet = RIVER
                RIVER -> onHandEnd()
            }
        }
    }

    private fun clearRoundData() {
        currentHand?.let { hand ->
            currentHand = hand.copy(
                currentRound = hand.currentRound?.copy(
                    currentMaxBet = 0,
                    playersInvestment = players.filter { it.playingStatus == PLAYING }
                        .map { player ->
                            PlayerInvestment(
                                playerSeatNo = player.seatNumber,
                                amount = 0
                            )
                        },
                    endsOn = getEndsOnPlayer()
                )
            )
        }
    }

    private fun getEndsOnPlayer(): Int {
        return currentHand?.smallBlindPlayer?.let { smallBlindPlayer ->
            val playingList =
                players.filter { it.playingStatus == PLAYING }.sortedBy { it.seatNumber }

            val isSmallBlindPlaying =
                playingList.firstOrNull { it.seatNumber == smallBlindPlayer } != null

            if (isSmallBlindPlaying) {
                smallBlindPlayer
            } else {
                playingList.firstOrNull { it.seatNumber > smallBlindPlayer }?.seatNumber
                    ?: (playingList.getOrNull(0)?.seatNumber ?: 0)
            }
        } ?: 0
    }

    private fun onHandEnd() {
        showPotDetails = true

    }

    private fun updatePlayerInvestment(seatNumber: Int, chips: Long, move: PlayerMove) {
        players = players.map { player ->
            if (player.seatNumber == seatNumber) {
                player.chips -= chips
            }
            player
        }.toMutableList()

        currentHand?.let { hand ->
            currentHand = hand.copy(
                pots = hand.pots.mapIndexed { index, pot ->
                    if (index == (currentHand?.pots?.size?.minus(1) ?: Long.MAX_VALUE)) {
                        pot.chips += chips
                        if (move == FOLD) {
                            pot.players.toMutableList().remove(seatNumber)
                        }
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
        val nextPlayer = playingList[(currentIndexInPlayingList + 1) % playingList.size]

        if (currentPlayer == nextPlayer) {
            onRoundEnd()
            onHandEnd()
        }

        players.firstOrNull { it.seatNumber == currentPlayer && (it.playingStatus == FOLDED || it.playingStatus == PlayingStatus.ALL_IN) }
            ?.let {
                updateEndsOn()
            }

        currentHand?.let { hand ->
            currentHand = hand.copy(
                currentPlayer = nextPlayer
            )
        }
        currentPlayer?.let {
            checkForRoundEnd(currentPlayer, nextPlayer)
        }
        getActionsForCurrentPlayer()
    }

    private fun updateEndsOn() {
        val currentEndsOn = currentHand?.currentRound?.endsOn

        val playingList =
            players.filter { it.playingStatus == PLAYING }.sortedBy { it.seatNumber }

        currentEndsOn?.let { current ->
            val nextEndsOn = playingList.firstOrNull { it.seatNumber > current }?.seatNumber
                ?: (playingList.getOrNull(0)?.seatNumber ?: 0)

            currentHand?.let { hand ->
                currentHand = hand.copy(
                    currentRound = hand.currentRound?.copy(
                        endsOn = nextEndsOn
                    )
                )
            }
        }
    }

    private fun checkForRoundEnd(oldPlayer: Int, newPlayer: Int) {
        currentHand?.let { hand ->
            if (hand.endOnBigBlind) {
                if (oldPlayer == hand.bigBlindPlayer) {
                    val oldPlayerMove =
                        hand.currentRound?.playersInvestment?.firstOrNull { it.playerSeatNo == oldPlayer }?.move
                    if (oldPlayerMove == CHECK || oldPlayerMove == FOLD) {
                        onRoundEnd()
                    }
                }
            } else {
                if (newPlayer == hand.currentRound?.endsOn) {
                    onRoundEnd()
                }
            }
        }
    }

    private fun getActionsForCurrentPlayer() {
        currentHand?.let { hand ->
            val currentMaxBet = hand.currentRound?.currentMaxBet ?: 0
            val investedAmount =
                hand.currentRound?.playersInvestment?.firstOrNull { it.playerSeatNo == hand.currentPlayer }?.amount
                    ?: 0
            val playerAmount =
                players.firstOrNull { it.seatNumber == hand.currentPlayer }?.chips ?: 0

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
            currentHand = hand.copy(
                previousBet = amount,
                currentRound = hand.currentRound?.copy(
                    currentMaxBet = (hand.currentRound?.currentMaxBet ?: 0) + amount,
                    endsOn = hand.currentPlayer,
                ),
                endOnBigBlind = false
            )
            updateCurrentPlayer()
        }
    }

    fun onRaise(amount: Long) {
        currentHand?.let { hand ->
            val currentMaxBet = hand.currentRound?.currentMaxBet ?: 0
            val investedAmount =
                hand.currentRound?.playersInvestment?.firstOrNull { it.playerSeatNo == hand.currentPlayer }?.amount
                    ?: 0

            callAmount = currentMaxBet - investedAmount

            updatePlayerInvestment(
                seatNumber = hand.currentPlayer,
                chips = callAmount + amount,
                move = RAISE
            )
            currentHand = hand.copy(
                previousBet = amount,
                currentRound = hand.currentRound?.copy(
                    currentMaxBet = (hand.currentRound?.currentMaxBet ?: 0) + amount,
                    endsOn = hand.currentPlayer,
                ),
                endOnBigBlind = false
            )
            updateCurrentPlayer()
        }
    }

    fun onFold() {
        val foldedPlayer = currentHand?.currentPlayer
        updateCurrentPlayer()
        players = players.map { player ->
            if (player.seatNumber == foldedPlayer) {
                player.playingStatus = FOLDED
            }
            player
        }.toMutableList()

        foldedPlayer?.let {
            updatePlayerInvestment(
                seatNumber = it,
                chips = 0,
                move = FOLD
            )
        }

        currentHand?.let { hand ->
            currentHand = hand.copy(
                pots = hand.pots.map { pot ->
                    pot.copy(
                        players = pot.players.filter { it != foldedPlayer }
                    )
                }
            )
        }
    }

    fun onAllIn() {
        currentHand?.let { hand ->
            val investedAmount =
                hand.currentRound?.playersInvestment?.firstOrNull { it.playerSeatNo == hand.currentPlayer }?.amount
                    ?: 0

            val playerAmount =
                players.firstOrNull { it.seatNumber == hand.currentPlayer }?.chips ?: 0

            updatePlayerInvestment(
                seatNumber = hand.currentPlayer,
                chips = playerAmount - investedAmount,
                move = ALL_IN
            )

            players = players.map { player ->
                if (player.seatNumber == hand.currentPlayer) {
                    player.playingStatus = PlayingStatus.ALL_IN
                }
                player
            }.toMutableList()
            updateCurrentPlayer()
        }
    }

    fun onAddWinner(potIndex: Int, player: Int) {
        if (potIndex <= winners.size) {
            winners.toMutableList().add(listOf<Int>().toMutableList())
        }

        winners = winners.mapIndexed { index, winner ->
            if (index == potIndex) {
                if (winners[potIndex].contains(player)) {
                    winner.toMutableList().remove(player)
                } else {
                    winner.toMutableList().add(player)
                }
            }
            winner
        }.toMutableList()
    }

    /**
     * Takes the Id of the table as a parameter for opening the specific table
     */
    @AssistedFactory
    interface RunningTableViewModelFactory {
        fun create(id: Long): RunningTableViewModel
    }
}

