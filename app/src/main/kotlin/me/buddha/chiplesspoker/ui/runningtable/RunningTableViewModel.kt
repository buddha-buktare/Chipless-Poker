package me.buddha.chiplesspoker.ui.runningtable

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = RunningTableViewModel.RunningTableViewModelFactory::class)
class RunningTableViewModel @AssistedInject constructor(
    @Assisted val id: Long,
) : ViewModel() {

    init {
        println("Id passed -> $id")
    }

    @AssistedFactory
    interface RunningTableViewModelFactory {
        fun create(id: Long): RunningTableViewModel
    }
}