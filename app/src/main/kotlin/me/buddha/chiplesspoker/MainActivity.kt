package me.buddha.chiplesspoker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import me.buddha.chiplesspoker.domain.navigation.Destination.CreateTable
import me.buddha.chiplesspoker.domain.navigation.Destination.RunningTable
import me.buddha.chiplesspoker.domain.navigation.NavigationService
import me.buddha.chiplesspoker.ui.createtable.CreateTableScreen
import me.buddha.chiplesspoker.ui.runningtable.RunningTableScreen
import me.buddha.chiplesspoker.ui.runningtable.RunningTableViewModel
import me.buddha.chiplesspoker.ui.theme.ChiplessPokerTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationService: NavigationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChiplessPokerTheme {
                NavHost(
                    navController = navigationService.navController,
                    startDestination = CreateTable,
                ) {
                    composable<CreateTable> {
                        CreateTableScreen()
                    }

                    composable<RunningTable> {
                        val args = it.toRoute<RunningTable>()
                        RunningTableScreen(
                            viewModel = hiltViewModel<RunningTableViewModel, RunningTableViewModel.RunningTableViewModelFactory> { factory ->
                                factory.create(args.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
