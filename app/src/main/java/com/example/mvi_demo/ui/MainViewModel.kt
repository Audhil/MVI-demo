package com.example.mvi_demo.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvi_demo.repository.MainRepo
import com.example.mvi_demo.ui.intent.MainIntent
import com.example.mvi_demo.ui.state.MainState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class MainViewModel
@ViewModelInject
constructor(
    private val repo: MainRepo
) : ViewModel() {

    private val _mutableStateFlow = MutableStateFlow<MainState>(MainState.Idle)

    val stateFlow: StateFlow<MainState>
        get() = _mutableStateFlow

    val intent: Channel<MainIntent> = Channel(capacity = Channel.UNLIMITED)

    init {
        listenIntent()
    }

    private fun listenIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    MainIntent.FetchUser -> {
                        _mutableStateFlow.value = MainState.Loading
                        _mutableStateFlow.value = try {
                            MainState.Users(repo.getUsersFromServer())
                        } catch (e: Exception) {
                            MainState.Error(e.localizedMessage)
                        }
                    }
                }
            }
        }
    }
}