package me.buddha.chiplesspoker.ui.runningtable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
        Text(text = "Current Blind")
        Text(text = "Current Street")

    }
}