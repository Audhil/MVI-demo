package com.example.mvi_demo.repository

import com.example.mvi_demo.data.api.ApiService
import javax.inject.Inject

class MainRepo
@Inject
constructor(
    private val apiService: ApiService
) {
    suspend fun getUsersFromServer() = apiService.getUsers()
}