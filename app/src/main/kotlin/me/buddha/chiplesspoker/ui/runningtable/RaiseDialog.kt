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
fun RaiseDialog(
    minRaise: Long, // Minimum raise required
    currentBet: Long, // Current total bet
    currentChips: Long, // Player's available chips
    onDismiss: () -> Unit,
    onAllIn: () -> Unit,
    onConfirmRaise: (Int) -> Unit
) {
    var raiseAmount by remember { mutableStateOf(minRaise) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Raise Your Bet") },
        text = {
            Column {
                Text("Current Bet: $currentBet")
                Text("Minimum Raise: $minRaise")
                Text("Your Chips: $currentChips")

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = raiseAmount.toString(),
                    onValueChange = { value ->
                        raiseAmount = value.toIntOrNull()?.toLong() ?: minRaise
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (raiseAmount < currentChips) {
                        onConfirmRaise(raiseAmount.toInt())
                    } else {
                        onAllIn()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (raiseAmount >= currentChips) Color.Red else Color.Blue
                )
            ) {
                Text(if (raiseAmount >= currentChips) "All-In" else "Raise $raiseAmount")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
