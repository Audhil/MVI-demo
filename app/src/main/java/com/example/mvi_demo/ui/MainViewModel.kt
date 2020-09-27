package com.example.mvi_demo.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvi_demo.repository.MainRepo
import com.example.mvi_demo.ui.state.*
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

    private val mainViewState by lazy {
        MainViewState(fetchState = FetchState.NotFetched, userList = emptyList())
    }

    private val _mutableStateFlow = MutableStateFlow(mainViewState)

    val stateFlow: StateFlow<MainViewState>
        get() = _mutableStateFlow

    val intent: Channel<UserIntent> = Channel(capacity = Channel.UNLIMITED)

    init {
        listenIntent()
    }

    private fun listenIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    UserIntent.FetchUsers ->
                        fetchUsers()
                }
            }
        }
    }

    private suspend fun fetchUsers() {
        _mutableStateFlow.value = mainViewState.copy(fetchState = FetchState.Fetching)
        try {
            val resp = repo.getUsersFromServer()
            _mutableStateFlow.value =
                mainViewState.copy(
                    fetchState = FetchState.Fetched,
                    userList = resp
                ) //  add proper reducer() to retain old values of list, I'm just replacing list old values
        } catch (e: Exception) {
            _mutableStateFlow.value =
                mainViewState.copy(
                    fetchState = FetchState.Error(e),
                )
        }
    }
}