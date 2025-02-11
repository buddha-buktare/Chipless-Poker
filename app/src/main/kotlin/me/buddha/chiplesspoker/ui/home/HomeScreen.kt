package me.buddha.chiplesspoker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import me.buddha.chiplesspoker.R
import me.buddha.chiplesspoker.ui.theme.ChiplessPokerTheme

@Composable
fun HomeScreen(
    navigateToCreateTable: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)), // Light background
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo Placeholder
        AsyncImage(
            model = R.raw.app_logo,
            contentDescription = null,
            modifier = Modifier.size(350.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Start New Game Button
        Button(
            onClick = { navigateToCreateTable() },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF0066FF)) // Primary Blue
        ) {
            Text("Start New Game", fontSize = 18.sp, color = Color.White)
        }

        Button(onClick = { throw RuntimeException("Test Crash!") }) {
            Text("Crash App")
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    ChiplessPokerTheme {
        HomeScreen(
            navigateToCreateTable = {}
        )
    }
}