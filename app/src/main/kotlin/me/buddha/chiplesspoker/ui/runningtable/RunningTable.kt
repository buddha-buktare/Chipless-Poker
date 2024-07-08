package me.buddha.chiplesspoker.ui.runningtable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RunningTableScreen(
    modifier: Modifier = Modifier,
    viewModel: RunningTableViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Text(text = "Current Blind : ${viewModel.blindStructure.blindLevels.getOrNull(0)?.big / viewModel.blindStructure.blindLevels.getOrNull(0).small}")
        // Text(text = "Current Street : ${viewModel.blindStructure.blindLevels}")

        viewModel.players.forEach { player ->
            Text(text = "${player.name}  ${player.chips}")
        }

        Text("Current Player -> ${viewModel.currentHand?.currentPlayer}")
        Button(onClick = viewModel::onCall) { Text("Call") }
        Button(onClick = viewModel::onCheck) { Text("Check") }
        Button(onClick = viewModel::onFold) { Text("Fold") }
        Button(onClick = viewModel::onBet) { Text("Bet") }
        Button(onClick = viewModel::onAllIn) { Text("AllIn") }

    }
}