package com.example.mvi_demo.ui.intent

sealed class MainIntent {
    object FetchUser : MainIntent()
}