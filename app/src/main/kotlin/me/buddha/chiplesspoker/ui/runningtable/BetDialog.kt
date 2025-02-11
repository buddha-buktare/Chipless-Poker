package me.buddha.chiplesspoker.ui.runningtable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun BetDialog(
    minBet: Long, // Minimum bet required
    currentChips: Long, // Player's total chips
    onDismiss: () -> Unit,
    onAllIn: () -> Unit,
    onConfirmBet: (Int) -> Unit
) {
    var betAmount by remember { mutableStateOf(minBet) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Place Your Bet") },
        text = {
            Column {
                Text("Minimum Bet: $minBet")
                Text("Your Chips: $currentChips")

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = betAmount.toString(),
                    onValueChange = { value ->
                        betAmount = value.toIntOrNull()?.toLong() ?: minBet
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (betAmount < currentChips) {
                        onConfirmBet(betAmount.toInt())
                    } else {
                        onAllIn()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (betAmount >= currentChips) Color.Red else Color.Green
                )
            ) {
                Text(if (betAmount >= currentChips) "All-In" else "Bet $betAmount")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
