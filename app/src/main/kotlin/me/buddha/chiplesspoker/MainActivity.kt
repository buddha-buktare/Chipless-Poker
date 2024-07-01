package me.buddha.chiplesspoker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import me.buddha.chiplesspoker.domain.Destination.CreateTable
import me.buddha.chiplesspoker.ui.createtable.CreateTableScreen
import me.buddha.chiplesspoker.ui.theme.ChiplessPokerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChiplessPokerTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = CreateTable,
                ) {
                    composable<CreateTable> {
                        CreateTableScreen()
                    }
                }
            }
        }
    }
}
