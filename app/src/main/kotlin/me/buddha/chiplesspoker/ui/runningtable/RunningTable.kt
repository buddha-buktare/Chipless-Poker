package me.buddha.chiplesspoker.ui.runningtable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.buddha.chiplesspoker.domain.utils.PlayerMove.ALL_IN
import me.buddha.chiplesspoker.domain.utils.PlayerMove.BET
import me.buddha.chiplesspoker.domain.utils.PlayerMove.CALL
import me.buddha.chiplesspoker.domain.utils.PlayerMove.CHECK
import me.buddha.chiplesspoker.domain.utils.PlayerMove.FOLD
import me.buddha.chiplesspoker.domain.utils.PlayerMove.RAISE

@Composable
fun RunningTableScreen(
    modifier: Modifier = Modifier,
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
            Button(onClick = { viewModel.onBet(35) }) { Text("Bet") }
        }
        if (viewModel.actionsForCurrentPlayer.contains(RAISE)) {
            Button(onClick = { viewModel.onRaise(35) }) { Text("Raise") }
        }
        if (viewModel.actionsForCurrentPlayer.contains(ALL_IN)) {
            Button(onClick = viewModel::onAllIn) { Text("AllIn") }
        }

        Text("Pot Amount: ${viewModel.currentHand?.pots?.sumOf { it.chips }}")

    }
}