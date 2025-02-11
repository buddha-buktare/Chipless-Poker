package me.buddha.chiplesspoker.ui.runningtable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConfirmExitDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {}, // Non-dismissible
        title = { Text("Exit Game?") },
        text = { Text("Are you sure you want to exit the running game? Progress may be lost.") },
        confirmButton = {
            Button(onClick = onConfirmExit) {
                Text("Exit")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
