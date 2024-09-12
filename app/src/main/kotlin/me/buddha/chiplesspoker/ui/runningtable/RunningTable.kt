package me.buddha.chiplesspoker.ui.runningtable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import me.buddha.chiplesspoker.domain.utils.PlayerMove.ALL_IN
import me.buddha.chiplesspoker.domain.utils.PlayerMove.BET
import me.buddha.chiplesspoker.domain.utils.PlayerMove.CALL
import me.buddha.chiplesspoker.domain.utils.PlayerMove.CHECK
import me.buddha.chiplesspoker.domain.utils.PlayerMove.FOLD
import me.buddha.chiplesspoker.domain.utils.PlayerMove.RAISE

@Composable
fun RunningTableScreen(
    viewModel: RunningTableViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Current Blind : ${viewModel.blindStructure.blindLevels.getOrNull(viewModel.blindStructure.currentLevel)?.big} / ${
                viewModel.blindStructure.blindLevels.getOrNull(
                    viewModel.blindStructure.currentLevel
                )?.small
            }"
        )
        if ((viewModel.blindStructure.currentLevel + 1) < viewModel.blindStructure.blindLevels.size) {
            Text(
                text = "Next Blind : ${viewModel.blindStructure.blindLevels.getOrNull(viewModel.blindStructure.currentLevel + 1)?.big} / ${
                    viewModel.blindStructure.blindLevels.getOrNull(
                        viewModel.blindStructure.currentLevel + 1
                    )?.small
                }"
            )
        }
        Text(text = "Current Street : ${viewModel.currentStreet.name}")

        viewModel.players.forEach { player ->
            if (player.seatNumber == -1) return@forEach
            Text(text = "${player.name}  ${player.chips} ${player.playingStatus.name}")
        }

        Text("Current Player -> ${viewModel.currentHand?.currentPlayer}")
        Text("Ends On -> ${viewModel.currentHand?.currentRound?.endsOn}")
        if (viewModel.actionsForCurrentPlayer.contains(CALL)) {
            Button(onClick = viewModel::onCall) { Text("Call ${viewModel.callAmount}") }
        }
        if (viewModel.actionsForCurrentPlayer.contains(CHECK)) {
            Button(onClick = viewModel::onCheck) { Text("Check") }
        }
        if (viewModel.actionsForCurrentPlayer.contains(FOLD)) {
            Button(onClick = viewModel::onFold) { Text("Fold") }
        }
        if (viewModel.actionsForCurrentPlayer.contains(BET)) {
            Row {
                var betValue by remember { mutableStateOf("") }
                Text("Min: ${viewModel.blindStructure.blindLevels[viewModel.blindStructure.currentLevel].big}")
                TextField(
                    value = betValue,
                    onValueChange = {
                        if (it.isDigitsOnly()) {
                            betValue = it
                        }
                    },
                    modifier = Modifier.width(150.dp)
                )
                Button(onClick = { viewModel.onBet(betValue.toLong()) }) { Text("Bet") }
            }
        }
        if (viewModel.actionsForCurrentPlayer.contains(RAISE)) {
            Row {
                var raiseValue by remember { mutableStateOf("") }
                Text("Min: ${viewModel.currentHand?.previousBet}")
                TextField(
                    value = raiseValue,
                    onValueChange = {
                        if (it.isDigitsOnly()) {
                            raiseValue = it
                        }
                    },
                    modifier = Modifier.width(150.dp)
                )
                Button(onClick = { viewModel.onRaise(raiseValue.toLong()) }) { Text("Raise") }
            }
        }
        if (viewModel.actionsForCurrentPlayer.contains(ALL_IN)) {
            Button(onClick = viewModel::onAllIn) { Text("AllIn") }
        }

        Text("Pot Amount: ${viewModel.currentHand?.pots?.sumOf { it.chips }}")

        if (viewModel.showPotDetails) {
            viewModel.currentHand?.pots?.forEachIndexed { index, pot ->

                Text(text = "${pot.chips}")
                pot.players.forEach { player ->
                    Text(
                        text = player.toString(),
                        modifier = Modifier
                            .background(Color.Cyan)
                            .clip(CircleShape)
                            .padding(16.dp)
                            .clickable { viewModel.onAddWinner(index, player) },
                    )
                }
            }

            viewModel.winners.forEachIndexed { index, winner ->
                Text(text = "Winner $index players")
                winner.forEach {
                    Text("$it")
                }
            }
            Button(onClick = viewModel::onDistribute) { Text("Distribute") }

        }

    }
}