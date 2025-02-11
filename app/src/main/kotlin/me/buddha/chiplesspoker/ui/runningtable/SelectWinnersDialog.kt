import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.buddha.chiplesspoker.domain.model.Player
import me.buddha.chiplesspoker.domain.model.Pot
import me.buddha.chiplesspoker.ui.runningtable.RunningTableViewModel

@Composable
fun SelectWinnersDialog(
    viewModel: RunningTableViewModel,
    pots: List<Pot>,  // Each pot has amount & potential winners
    selectedWinnersMap: Map<Pot, List<Player>> = mapOf(), // Pre-selected winners
    onConfirmSelection: (Map<Pot, List<Player>>) -> Unit = {}
) {
    var selectedPlayersMap by remember {
        mutableStateOf(selectedWinnersMap.mapValues { it.value.toMutableSet() })
    }
    val context = LocalContext.current

    // val allPotsHaveWinners by derivedStateOf {
    //     selectedPlayersMap.all { it.value.isNotEmpty() } // Ensure all pots have winners
    // }

    AlertDialog(
        onDismissRequest = {}, // Non-dismissible
        title = { Text("Select Winners for Each Pot") },
        text = {

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.8f), // Limit height so it doesn’t take full screen
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(pots) { index, pot ->
                    Column {
                        Text(
                            "Pot Amount: ${pot.chips}",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pot.players.chunked(2)
                                .forEach { rowPlayers -> // Splitting into rows of 2
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        rowPlayers.forEach { player ->
                                            Button(
                                                onClick = { viewModel.onAddWinner(index, player) },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (viewModel.winners[index].contains(
                                                            player
                                                        )
                                                    ) Color.Green else Color.Gray
                                                ),
                                                modifier = Modifier.weight(1f) // Equal width for buttons
                                            ) {
                                                Text(
                                                    viewModel.players.find { it.seatNumber == player }?.name
                                                        ?: ""
                                                )
                                            }
                                        }
                                        // Fill remaining space if row has only one button
                                        if (rowPlayers.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.onConfirmWinnerSelection(context) },
                enabled = true // Enable only if all pots have at least one winner
            ) {
                Text("Confirm Selection")
            }
        }
    )
}
