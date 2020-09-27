package com.example.mvi_demo.ui.state

import com.example.mvi_demo.model.User

data class MainViewState(
    val fetchState: FetchState? = null,
    val userList: List<User>? = null
)

sealed class FetchState {
    object Fetching : FetchState()
    object Fetched : FetchState()
    object NotFetched : FetchState()

    data class Error(val msg: String) : FetchState() {
        constructor(e: Throwable) : this(e.message ?: "")
    }
}

sealed class UserIntent {
    object FetchUsers : UserIntent()
}