package me.buddha.chiplesspoker.ui.runningtable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.buddha.chiplesspoker.domain.model.Player

@Composable
fun FinalDistributionDialog(
    players: List<Player>,
    winnings: Map<Int, Long>, // Player -> Amount Won
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {}, // Non-dismissible
        title = { Text("Distribute Winnings") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                winnings.forEach { (player, amount) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            players.find { it.seatNumber == player }?.name ?: "",
                            fontWeight = FontWeight.Bold
                        )
                        Text("Won: $amount", color = Color.Green)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                // enabled = allDistributed // Only enable if all winnings are distributed
            ) {
                Text("Confirm Distribution")
            }
        }
    )
}
