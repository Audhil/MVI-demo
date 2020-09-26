package com.example.mvi_demo.data.api

import com.example.mvi_demo.model.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}