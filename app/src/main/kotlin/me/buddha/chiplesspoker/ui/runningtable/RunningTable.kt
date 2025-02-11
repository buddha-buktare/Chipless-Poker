package me.buddha.chiplesspoker.ui.runningtable

import SelectWinnersDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.buddha.chiplesspoker.domain.utils.PlayerMove.ALL_IN
import me.buddha.chiplesspoker.domain.utils.PlayerMove.BET
import me.buddha.chiplesspoker.domain.utils.PlayerMove.CALL
import me.buddha.chiplesspoker.domain.utils.PlayerMove.CHECK
import me.buddha.chiplesspoker.domain.utils.PlayerMove.FOLD
import me.buddha.chiplesspoker.domain.utils.PlayerMove.RAISE
import me.buddha.chiplesspoker.ui.runningtable.DialogType.CONFIRM_EXIT
import me.buddha.chiplesspoker.ui.runningtable.DialogType.DISTRIBUTE
import me.buddha.chiplesspoker.ui.runningtable.DialogType.NONE
import me.buddha.chiplesspoker.ui.runningtable.DialogType.SELECT_WINNERS
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RunningTable(
    viewModel: RunningTableViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(NONE) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Game Stats
            // Text("Blinds: ${viewModel.blindStructure.blindLevels[0].small} / $${viewModel.blindStructure.blindLevels[0].small}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                "Street: ${viewModel.currentStreet}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            // Text("Open Cards: $openCards", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF388E3C))
            Text(
                "Pot: ${viewModel.currentHand?.pots?.sumOf { it.chips }} chips",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD32F2F)
            )

            // Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))

            // Circular Player Seating
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                // .height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                val angleStep = 360f / viewModel.players.size

                viewModel.players.forEachIndexed { index, player ->
                    val angle = Math.toRadians((angleStep * index - 90).toDouble())
                    val xOffset = 150 * cos(angle).toFloat()
                    val yOffset = 150 * sin(angle).toFloat()

                    Box(
                        modifier = Modifier
                            .offset(xOffset.dp, yOffset.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        PlayerCard(
                            player = player,
                            currentHand = viewModel.currentHand,
                            isCurrentTurn = (index == viewModel.currentHand?.currentPlayer),
                            onAddPlayer = { /* Open Add Player Dialog */ }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom,
            ) {
                if (viewModel.actionsForCurrentPlayer.contains(CALL)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "${viewModel.callAmount}")
                        Button(onClick = viewModel::onCall) { Text("Call") }
                    }
                }
                if (viewModel.actionsForCurrentPlayer.contains(CHECK)) {
                    Button(onClick = viewModel::onCheck) { Text("Check") }
                }
                if (viewModel.actionsForCurrentPlayer.contains(FOLD)) {
                    Button(onClick = viewModel::onFold) { Text("Fold") }
                }
                if (viewModel.actionsForCurrentPlayer.contains(BET)) {
                    Button(
                        onClick = {
                            showDialog = true
                            dialogType = DialogType.BET
                        }
                    ) { Text("Bet") }
                }
                if (viewModel.actionsForCurrentPlayer.contains(RAISE)) {
                    Button(
                        onClick = {
                            showDialog = true
                            dialogType = DialogType.RAISE
                        }
                    ) { Text("Raise") }
                }
                if (viewModel.actionsForCurrentPlayer.contains(ALL_IN)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = viewModel.allInText)
                        Button(onClick = viewModel::onAllIn) { Text("AllIn") }
                    }
                }
            }
        }

        if (viewModel.showPotDetails) {
            showDialog = true
            dialogType = SELECT_WINNERS
        }

        if (viewModel.showFinalDistributionDialog) {
            showDialog = true
            dialogType = DISTRIBUTE
        }

        // Back Confirmation Dialog
        if (showDialog) {
            when (dialogType) {
                NONE -> {}
                DialogType.BET -> {
                    BetDialog(
                        minBet = viewModel.blindStructure.blindLevels[viewModel.blindStructure.currentLevel].big,
                        currentChips = viewModel.players.find { it.seatNumber == viewModel.currentHand?.currentPlayer }?.chips
                            ?: 0,
                        onDismiss = { showDialog = false },
                        onAllIn = {
                            viewModel.onAllIn()
                            showDialog = false
                        },
                        onConfirmBet = { amount ->
                            viewModel.onBet(amount.toLong(), context)
                            showDialog = false
                        }
                    )
                }

                DialogType.RAISE -> {
                    RaiseDialog(
                        minRaise = viewModel.blindStructure.blindLevels[viewModel.blindStructure.currentLevel].big,
                        currentBet = viewModel.currentHand?.currentRound?.currentMaxBet ?: 0,
                        currentChips = viewModel.players.find { it.seatNumber == viewModel.currentHand?.currentPlayer }?.chips
                            ?: 0,
                        onDismiss = { showDialog = false },
                        onAllIn = {
                            viewModel.onAllIn()
                            showDialog = false
                        },
                        onConfirmRaise = { amount ->
                            viewModel.onRaise(amount.toLong(), context)
                            showDialog = false
                        }
                    )
                }

                SELECT_WINNERS -> {
                    SelectWinnersDialog(
                        viewModel = viewModel,
                        pots = viewModel.currentHand?.pots ?: listOf(),
                        // selectedWinnersMap = selectedWinnersMap,
                        // onConfirmSelection = { selectedWinners ->
                        //     selectedWinnersMap = selectedWinners
                        //     showDialog = false // Close the dialog after selection
                        // }
                    )
                }

                DISTRIBUTE -> {
                    FinalDistributionDialog(
                        players = viewModel.players,
                        winnings = viewModel.individualWinners,
                        onConfirm = {
                            showDialog = false
                            viewModel.onConfirmDistribution()
                        }
                    )
                }

                CONFIRM_EXIT -> {
                    ConfirmExitDialog(
                        onConfirmExit = { viewModel.onConfirmExit() },
                        onDismiss = {
                            showDialog = false
                            dialogType = NONE
                        }
                    )
                }
            }
        }
    }

    BackHandler {
        showDialog = true
        dialogType = CONFIRM_EXIT
    }
}

enum class DialogType {
    NONE,
    BET,
    RAISE,
    SELECT_WINNERS,
    DISTRIBUTE,
    CONFIRM_EXIT,
}