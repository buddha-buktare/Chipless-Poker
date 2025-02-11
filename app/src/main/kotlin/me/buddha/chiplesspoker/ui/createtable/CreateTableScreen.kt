package me.buddha.chiplesspoker.ui.createtable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.EMPTY

@Composable
fun CreateTableScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateTableViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedPlayerIndex by remember { mutableStateOf(-1) }
    var playerName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp)) // Space at the top

        Text("Create Table", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Buy-In Input
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = viewModel.initialBuyInAmount.toString(),
                onValueChange = { value ->
                    if (value.isNotEmpty() && value.toInt() > 0) {
                        viewModel.updateInitialBuyIn(value.toLong())
                    }
                },
                label = { Text("Initial Buy-In") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("chips", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Big Blind Input
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = viewModel.blindStructure.blindLevels[0].big.toString(),
                onValueChange = { value ->
                    if (value.isNotEmpty() && value.isDigitsOnly()) {
                        viewModel.updateLevelBigBlind(0, value.toLong())
                    }
                },
                label = { Text("Big Blind") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("chips", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Small Blind Input
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = viewModel.blindStructure.blindLevels[0].small.toString(),
                onValueChange = { value ->
                    if (value.isNotEmpty() && value.isDigitsOnly()) {
                        viewModel.updateLevelSmallBlind(0, value.toLong())
                    }
                },
                label = { Text("Small Blind") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("chips", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Players List (Fixed 6 Tiles)
        Text("Players", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columns, 3 rows
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(6) { index ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .background(
                            if (viewModel.players[index].playingStatus == EMPTY) Color(
                                0xFFE0E0E0
                            ).copy(alpha = 0.5f) else Color.White /* Grayed out if no name */
                        ),
                    shape = RoundedCornerShape(8.dp),
                    // elevation = 4.dp,
                ) {
                    Box {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (viewModel.players[index].playingStatus == EMPTY) "Player ${index + 1}" else viewModel.players[index].name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (viewModel.players[index].playingStatus == EMPTY) Color.Gray else Color.Black // Gray text if disabled
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    selectedPlayerIndex = index
                                    playerName =
                                        if (viewModel.players[index].name == "Player") "" else viewModel.players[index].name
                                    showDialog = true
                                },
                                enabled = true // Always allow editing even if it's disabled visually
                            ) {
                                Text(if (viewModel.players[index].playingStatus == EMPTY) "Add" else "Edit")
                            }
                        }
                    }
                }
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        // Start Game Button
        Button(
            onClick = { viewModel.startTable(context) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFF00C853))
        ) {
            Text("Start Game", color = Color.White)
        }
    }

    // Player Name Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Enter Player Name") },
            text = {
                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it },
                    label = { Text("Player Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addPlayer(selectedPlayerIndex, playerName)
                        showDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = {
                    viewModel.removePlayer(selectedPlayerIndex)
                    showDialog = false
                }) {
                    Text("Remove")
                }
            }
        )
    }
}
