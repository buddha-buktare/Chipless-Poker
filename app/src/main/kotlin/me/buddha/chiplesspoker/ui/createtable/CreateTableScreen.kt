package me.buddha.chiplesspoker.ui.createtable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import me.buddha.chiplesspoker.domain.usecase.DurationUnit.HANDS
import me.buddha.chiplesspoker.domain.usecase.DurationUnit.MINUTES

@Composable
fun CreateTableScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateTableViewModel = hiltViewModel(),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Initial BuyIn")
                TextField(
                    value = viewModel.initialBuyInAmount.toString(),
                    onValueChange = { value ->
                        if (value.toInt() > 0) {
                            viewModel.updateInitialBuyIn(value.toLong())
                        }
                    },
                )
                Text(text = "chips")
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Blind Structure")
                TextField(
                    value = viewModel.blindStructure.durationUnit.value,
                    onValueChange = { value ->
                        if (value.toInt() == 0) {
                            viewModel.updateDurationUnit(HANDS)
                        } else {
                            viewModel.updateDurationUnit(MINUTES)
                        }
                    }
                )
                Button(
                    onClick = { viewModel.addBlindLevel() },
                ) {
                    Text(text = "Add Level")
                }
                viewModel.blindStructure.blindLevels.forEachIndexed { index, level ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                    ) {
                        Text(text = level.level.toString())
                        TextField(
                            value = level.big.toString(),
                            onValueChange = { value ->
                                if (value.isDigitsOnly()) {
                                    viewModel.updateLevelBigBlind(index, value.toLong())
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.width(100.dp)
                        )
                        TextField(
                            value = level.small.toString(),
                            onValueChange = { value ->
                                if (value.isDigitsOnly()) {
                                    viewModel.updateLevelSmallBlind(index, value.toLong())
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.width(100.dp)
                        )
                        TextField(
                            value = level.duration.toString(),
                            onValueChange = { value ->
                                if (value.isDigitsOnly()) {
                                    viewModel.updateLevelDuration(index, value.toLong())
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.width(100.dp)
                        )
                        Button(
                            modifier = Modifier.width(100.dp),
                            onClick = { viewModel.removeBlindLevel(index) }
                        ) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
        item {
            Text(text = "Players")
            Button(
                onClick = {
                    viewModel.addPlayer(
                        viewModel.players.size,
                        "Player ${viewModel.players.size + 1}"
                    )
                },
            ) {
                Text(text = "Add Player")
            }
            viewModel.players.forEachIndexed { index, player ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                ) {
                    TextField(
                        value = player.name,
                        onValueChange = { value ->
                            if (value.isDigitsOnly()) {
                                viewModel.updateLevelBigBlind(index, value.toLong())
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.width(100.dp)
                    )
                    Button(
                        modifier = Modifier.width(100.dp),
                        onClick = { viewModel.removePlayer(index) }
                    ) {
                        Text("Remove")
                    }
                }
            }

        }
        item {
            Button(
                onClick = { viewModel.startTable() }
            ) {
                Text("Start Table")
            }
        }
    }
}